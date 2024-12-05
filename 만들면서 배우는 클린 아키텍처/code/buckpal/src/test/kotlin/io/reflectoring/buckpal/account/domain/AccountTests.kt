package io.reflectoring.buckpal.account.domain

import io.reflectoring.buckpal.common.AccountTestData.defaultAccount
import io.reflectoring.buckpal.common.ActivityTestData.defaultActivity
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AccountTests {

    @Test
    fun `calculates balance`() {
        val accountId = Account.AccountId(1)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(ActivityWindow(
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(999)).build(),
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(1)).build()))
            .build()

        val balance = account.calculateBalance()

        assertEquals(balance, Money.of(1555))
    }

    @Test
    fun `withdrawal succeeds`() {
        val accountId = Account.AccountId(1)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(ActivityWindow(
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(999)).build(),
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(1)).build()))
            .build()

        val success = account.withdraw(Money.of(555), Account.AccountId(99))

        assertTrue(success)
        assertEquals(account.activityWindow.getActivities().size, 3)
        assertEquals(account.calculateBalance(), Money.of(1000))
    }

    @Test
    fun `withdrawal failure`() {
        val accountId = Account.AccountId(1)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(ActivityWindow(
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(999)).build(),
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(1)).build()))
            .build()

        val success = account.withdraw(Money.of(1556), Account.AccountId(99))

        assertFalse(success)
        assertEquals(account.activityWindow.getActivities().size, 2)
        assertEquals(account.calculateBalance(), Money.of(1555))
    }

    @Test
    fun `deposit success`() {
        val accountId = Account.AccountId(1)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555))
            .withActivityWindow(ActivityWindow(
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(999)).build(),
                defaultActivity()
                    .withTargetAccountId(accountId)
                    .withMoney(Money.of(1)).build()))
            .build()

        val success = account.deposit(Money.of(445), Account.AccountId(99))

        assertTrue(success)
        assertEquals(account.activityWindow.getActivities().size, 3)
        assertEquals(account.calculateBalance(), Money.of(2000))
    }
}