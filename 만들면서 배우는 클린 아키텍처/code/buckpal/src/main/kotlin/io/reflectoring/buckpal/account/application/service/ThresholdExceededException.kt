package io.reflectoring.buckpal.account.application.service

import io.reflectoring.buckpal.account.domain.Money

class ThresholdExceededException(
    threshold: Money,
    actual: Money
) : RuntimeException("Maximum threshold for money transfer exceeded: tried to transfer $actual but threshold is $threshold")
