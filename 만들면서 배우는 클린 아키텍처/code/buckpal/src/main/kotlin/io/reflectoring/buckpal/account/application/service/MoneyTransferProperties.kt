package io.reflectoring.buckpal.account.application.service

import io.reflectoring.buckpal.account.domain.Money

data class MoneyTransferProperties(
    var maximumTransferThreshold: Money = Money.of(1_000_000L)
)
