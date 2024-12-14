package io.reflectoring.buckpal.account.domain

import java.math.BigInteger

data class Money(val amount: BigInteger) {

    companion object {
        val ZERO: Money = Money(BigInteger.ZERO)

        fun of(value: Long): Money = Money(BigInteger.valueOf(value))

        fun add(a: Money, b: Money): Money = Money(a.amount + b.amount)

        fun subtract(a: Money, b: Money): Money = Money(a.amount - b.amount)
    }

    fun isPositiveOrZero(): Boolean {
        return this.amount >= BigInteger.ZERO
    }

    fun isNegative(): Boolean {
        return this.amount < BigInteger.ZERO
    }

    fun isPositive(): Boolean {
        return this.amount > BigInteger.ZERO
    }

    fun isGreaterThanOrEqualTo(money: Money): Boolean {
        return this.amount >= money.amount
    }

    fun isGreaterThan(money: Money): Boolean {
        return this.amount > money.amount
    }

    operator fun minus(money: Money): Money = Money(amount - money.amount)

    operator fun plus(money: Money): Money = Money(amount + money.amount)

    fun negate(): Money = Money(amount.negate())
}