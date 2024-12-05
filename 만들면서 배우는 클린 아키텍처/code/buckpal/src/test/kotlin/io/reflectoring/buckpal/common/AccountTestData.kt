package io.reflectoring.buckpal.common

import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.ActivityWindow
import io.reflectoring.buckpal.account.domain.Money

object AccountTestData {

    fun defaultAccount(): AccountBuilder {
        return AccountBuilder()
            .withAccountId(Account.AccountId(42))
            .withBaselineBalance(Money.of(999))
            .withActivityWindow(
                ActivityWindow(
                    ActivityTestData.defaultActivity().build(),
                    ActivityTestData.defaultActivity().build(),
                )
            )
    }

    class AccountBuilder {
        private var accountId: Account.AccountId? = null
        private var baselineBalance: Money? = null
        private var activityWindow: ActivityWindow? = null

        fun withAccountId(accountId: Account.AccountId) = apply { this.accountId = accountId }
        fun withBaselineBalance(baselineBalance: Money) = apply { this.baselineBalance = baselineBalance }
        fun withActivityWindow(activityWindow: ActivityWindow) = apply { this.activityWindow = activityWindow }

        fun build(): Account {
            requireNotNull(accountId) { "AccountId must not be null" }
            requireNotNull(baselineBalance) { "Baseline balance must not be null" }
            requireNotNull(activityWindow) { "ActivityWindow must not be null" }

            return Account.withId(accountId!!, baselineBalance!!, activityWindow!!)
        }
    }
}