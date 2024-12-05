package io.reflectoring.buckpal.account.domain

import io.reflectoring.buckpal.common.ActivityTestData.defaultActivity
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

class ActivityWindowTests {
    @Test
    fun `calculates start timestamp`() {
        val window = ActivityWindow(
            defaultActivity().withTimestamp(startDate()).build(),
            defaultActivity().withTimestamp(inBetweenDate()).build(),
            defaultActivity().withTimestamp(endDate()).build()
        )

        assertEquals(window.startTimestamp, startDate())
    }

    @Test
    fun `calculates end timestamp`() {
        val window = ActivityWindow(
            defaultActivity().withTimestamp(startDate()).build(),
            defaultActivity().withTimestamp(inBetweenDate()).build(),
            defaultActivity().withTimestamp(endDate()).build()
        )

        assertEquals(window.endTimestamp, endDate())
    }

    @Test
    fun `calculates balance`() {
        val account1 = Account.AccountId(1)
        val account2 = Account.AccountId(2)

        val window = ActivityWindow(
            defaultActivity()
                .withSourceAccount(account1)
                .withTargetAccountId(account2)
                .withMoney(Money.of(999)).build(),
            defaultActivity()
                .withSourceAccount(account1)
                .withTargetAccountId(account2)
                .withMoney(Money.of(1)).build(),
            defaultActivity()
                .withSourceAccount(account2)
                .withTargetAccountId(account1)
                .withMoney(Money.of(500)).build(),
        )

        assertEquals(window.calculateBalance(account1), Money.of(-500))
        assertEquals(window.calculateBalance(account2), Money.of(500))
    }

    private fun startDate(): LocalDateTime {
        return LocalDateTime.of(2019, 8, 3, 0, 0)
    }

    private fun inBetweenDate(): LocalDateTime {
        return LocalDateTime.of(2019, 8, 4, 0, 0)
    }

    private fun endDate(): LocalDateTime {
        return LocalDateTime.of(2019, 8, 5, 0, 0)
    }
}