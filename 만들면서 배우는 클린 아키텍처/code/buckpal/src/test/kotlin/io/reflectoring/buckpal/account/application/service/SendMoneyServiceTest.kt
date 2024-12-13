package io.reflectoring.buckpal.account.application.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyCommand
import io.reflectoring.buckpal.account.application.port.out.AccountLock
import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.account.application.port.out.UpdateAccountStatePort
import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.Money
import java.time.LocalDateTime
import kotlin.test.*

class SendMoneyServiceTest {

    private val loadAccountPort: LoadAccountPort = mockk()
    private val accountLock: AccountLock = mockk()
    private val updateAccountStatePort: UpdateAccountStatePort = mockk()

    private val sendMoneyService = SendMoneyService(
        loadAccountPort,
        accountLock,
        updateAccountStatePort,
        moneyTransferProperties(),
    )

    @BeforeTest
    fun setUp() {
        every { accountLock.lockAccount(any()) } returns Unit
        every { accountLock.releaseAccount(any()) } returns Unit

        every { updateAccountStatePort.updateActivities(any()) } returns Unit
    }

    @Test
    fun `given withdrawal fails, then only source account is locked and released`() {
        val sourceAccountId = Account.AccountId(41L)
        val sourceAccount = givenAnAccountWithId(sourceAccountId)

        val targetAccountId = Account.AccountId(42L)
        val targetAccount = givenAnAccountWithId(targetAccountId)

        givenWithdrawalWillFail(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val command = SendMoneyCommand(
            sourceAccountId,
            targetAccountId,
            Money.of(300L)
        )

        val success = sendMoneyService.sendMoney(command)

        assertFalse { success }

        verify { accountLock.lockAccount(sourceAccountId) }
        verify { accountLock.releaseAccount(sourceAccountId) }
        verify(exactly = 0) { accountLock.lockAccount(targetAccountId) }
    }

    @Test
    fun `transaction succeeds`() {
        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()

        givenWithdrawalWillSucceed(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val money = Money.of(500L)
        val command = SendMoneyCommand(
            sourceAccount.id!!,
            targetAccount.id!!,
            money
        )

        val success = sendMoneyService.sendMoney(command)

        assertTrue(success)

        val sourceAccountId = sourceAccount.id!!
        val targetAccountId = targetAccount.id!!

        verify { accountLock.lockAccount(sourceAccountId) }
        verify { sourceAccount.withdraw(money, targetAccountId) }
        verify { accountLock.releaseAccount(sourceAccountId) }

        verify { accountLock.lockAccount(targetAccountId) }
        verify { targetAccount.deposit(money, sourceAccountId) }
        verify { accountLock.releaseAccount(targetAccountId) }

        thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId)
    }

    private fun thenAccountsHaveBeenUpdated(vararg accountIds: Account.AccountId) {
        val capturedAccount = mutableListOf<Account>()
        verify(exactly = accountIds.size) { updateAccountStatePort.updateActivities(capture(capturedAccount)) }

        val updatedAccountIds = capturedAccount.map { it.id }
        accountIds.forEach { accountId ->
            assertContains(updatedAccountIds, accountId)
        }
    }

    private fun givenDepositWillSucceed(account: Account) {
        every { account.deposit(any(), any()) } returns true
    }

    private fun givenWithdrawalWillFail(account: Account) {
        every { account.withdraw(any(), any()) } returns false
    }

    private fun givenWithdrawalWillSucceed(account: Account) {
        every { account.withdraw(any(), any()) } returns true
    }

    private fun givenTargetAccount(): Account {
        return givenAnAccountWithId(Account.AccountId(42L))
    }

    private fun givenSourceAccount(): Account {
        return givenAnAccountWithId(Account.AccountId(41L))
    }

    private fun givenAnAccountWithId(id: Account.AccountId): Account {
        val account = mockk<Account>()
        every { account.id } returns id
        every { loadAccountPort.loadAccount(eq(id), any<LocalDateTime>()) } returns account
        return account
    }

    private fun moneyTransferProperties() = MoneyTransferProperties(
        Money.of(Long.MAX_VALUE)
    )

}
