package io.reflectoring.buckpal.common

import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.Activity
import io.reflectoring.buckpal.account.domain.Money
import java.time.LocalDateTime

object ActivityTestData {

    fun defaultActivity(): ActivityBuilder {
        return ActivityBuilder()
            .withOwnerAccount(Account.AccountId(42))
            .withSourceAccount(Account.AccountId(42))
            .withTargetAccountId(Account.AccountId(42))
            .withTimestamp(LocalDateTime.now())
            .withMoney(Money.of(999))
    }

    class ActivityBuilder {
        private var id: Activity.ActivityId? = null
        private lateinit var ownerAccountId: Account.AccountId
        private lateinit var sourceAccountId: Account.AccountId
        private lateinit var targetAccountId: Account.AccountId
        private lateinit var timestamp: LocalDateTime
        private lateinit var money: Money

        fun withId(id: Activity.ActivityId) = apply { this.id = id }
        fun withOwnerAccount(ownerAccountId: Account.AccountId) = apply { this.ownerAccountId = ownerAccountId }
        fun withSourceAccount(sourceAccountId: Account.AccountId) = apply { this.sourceAccountId = sourceAccountId }
        fun withTargetAccountId(accountId: Account.AccountId) = apply { this.targetAccountId = accountId }
        fun withTimestamp(timestamp: LocalDateTime) = apply { this.timestamp = timestamp }
        fun withMoney(money: Money) = apply { this.money = money }

        fun build(): Activity {
            return Activity(
                id = id,
                ownerAccountId = ownerAccountId,
                sourceAccountId = sourceAccountId,
                targetAccountId = targetAccountId,
                timestamp = timestamp,
                money = money
            )
        }
    }
}