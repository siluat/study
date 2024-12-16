package io.reflectoring.buckpal.account.domain

import java.time.LocalDateTime

data class Account(
    val id: AccountId?,
    val baselineBalance: Money,
    var activityWindow: ActivityWindow,
) {

    companion object {
        fun withoutId(baselineBalance: Money, activityWindow: ActivityWindow): Account =
            Account(null, baselineBalance, activityWindow)

        fun withId(accountId: AccountId, baselineBalance: Money, activityWindow: ActivityWindow): Account =
            Account(accountId, baselineBalance, activityWindow)
    }

    fun calculateBalance(): Money {
        requireNotNull(id) { "Account ID must not be null" }

        return baselineBalance + activityWindow.calculateBalance(id)
    }

    fun withdraw(money: Money, targetAccountId: AccountId): Boolean {
        requireNotNull(id) { "Account ID must not be null" }

        if (!mayWithdraw(money)) return false

        val withdrawal = Activity(
            ownerAccountId = id,
            sourceAccountId = id,
            targetAccountId = targetAccountId,
            timestamp = LocalDateTime.now(),
            money = money,
        )
        activityWindow.addActivity(withdrawal)
        return true
    }

    private fun mayWithdraw(money: Money): Boolean =
        (calculateBalance() + money.negate()).isPositiveOrZero()

    fun deposit(money: Money, sourceAccountId: AccountId): Boolean {
        requireNotNull(id) { "Account ID must not be null" }

        val deposit = Activity(
            ownerAccountId = id,
            sourceAccountId = sourceAccountId,
            targetAccountId = id,
            timestamp = LocalDateTime.now(),
            money = money,
        )
        activityWindow.addActivity(deposit)
        return true
    }

    data class AccountId(val value: Long)
}