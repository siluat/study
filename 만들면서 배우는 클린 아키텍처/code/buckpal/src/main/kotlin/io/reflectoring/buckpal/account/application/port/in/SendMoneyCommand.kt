package io.reflectoring.buckpal.account.application.port.`in`

import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.Money

data class SendMoneyCommand(
    val sourceAccountId: Account.AccountId,
    val targetAccountId: Account.AccountId,
    val money: Money
) {

    init {
        require(money.isPositive()) { "The amount of money to transfer must be greater than zero" }
    }

}
