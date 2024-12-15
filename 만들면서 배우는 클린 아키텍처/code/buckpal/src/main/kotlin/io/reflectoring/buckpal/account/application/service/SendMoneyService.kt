package io.reflectoring.buckpal.account.application.service

import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyCommand
import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.account.application.port.out.AccountLock
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort
import io.reflectoring.buckpal.common.UseCase
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@UseCase
@Transactional
/**
 * 서비스는 인커밍 포트 인터페이스인 SendMoneyUseCase를 구현한다
 */
class SendMoneyService(
    /**
     * 계좌를 불러오기 위해 아웃고잉 포트 인터페이스인 LoadAccountPort를 사용한다
     */
    val loadAccountPort: LoadAccountPort,
    val accountLock: AccountLock,
    /**
     * 데이터베이스의 계좌 상태를 업데이트하기 위해
     * 아웃고잉 포트 인터페이스인 UpdateAccountStatePort를 사용한다
     */
    val updateAccountStatePort: UpdateAccountStatePort,
    val moneyTransferProperties: MoneyTransferProperties
) : SendMoneyUseCase {

    override fun sendMoney(command: SendMoneyCommand): Boolean {

        checkThreshold(command)

        val baselineDate = LocalDateTime.now().minusDays(10)

        val sourceAccount = loadAccountPort.loadAccount(command.sourceAccountId, baselineDate)
        val targetAccount = loadAccountPort.loadAccount(command.targetAccountId, baselineDate)

        val sourceAccountId = sourceAccount.id
            ?: throw IllegalStateException("source account not found")
        val targetAccountId = targetAccount.id
            ?: throw IllegalStateException("target account not found")

        accountLock.lockAccount(sourceAccountId)
        if (!sourceAccount.withdraw(command.money, targetAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            return false
        }

        accountLock.lockAccount(targetAccountId)
        if (!targetAccount.deposit(command.money, sourceAccountId)) {
            accountLock.releaseAccount(sourceAccountId)
            accountLock.releaseAccount(targetAccountId)
            return false
        }

        updateAccountStatePort.updateActivities(sourceAccount)
        updateAccountStatePort.updateActivities(targetAccount)

        accountLock.releaseAccount(sourceAccountId)
        accountLock.releaseAccount(targetAccountId)

        return true
    }

    private fun checkThreshold(command: SendMoneyCommand) {
        if (command.money.isGreaterThan(moneyTransferProperties.maximumTransferThreshold)) {
            throw ThresholdExceededException(moneyTransferProperties.maximumTransferThreshold, command.money)
        }
    }

}
