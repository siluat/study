package io.reflectoring.buckpal.account.adapter.out.persistence

import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort
import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.common.PersistenceAdapter
import jakarta.persistence.EntityNotFoundException
import java.time.LocalDateTime

@PersistenceAdapter
class AccountPersistenceAdapter(
    private val accountRepository: SpringDataAccountRepository,
    private val activityRepository: ActivityRepository,
    private val accountMapper: AccountMapper,
) : LoadAccountPort, UpdateAccountStatePort {

    override fun loadAccount(accountId: Account.AccountId, baselineDate: LocalDateTime): Account {
        val account = accountRepository.findById(accountId.value)
            .orElseThrow { EntityNotFoundException() }

        val activities = activityRepository.findByOwnerSince(accountId.value, baselineDate)

        val withdrawalBalance = activityRepository.getWithdrawalBalanceUntil(accountId.value, baselineDate).orZero()
        val depositBalance = activityRepository.getDepositBalanceUntil(accountId.value, baselineDate).orZero()

        return accountMapper.mapToDomainEntity(account, activities, withdrawalBalance, depositBalance)
    }

    private fun Long?.orZero(): Long = this ?: 0L

    override fun updateActivities(account: Account) {
        account.activityWindow.getActivities()
            .filter { it.id == null }
            .forEach { activityRepository.save(accountMapper.mapToJpaEntity(it)) }
    }

}
