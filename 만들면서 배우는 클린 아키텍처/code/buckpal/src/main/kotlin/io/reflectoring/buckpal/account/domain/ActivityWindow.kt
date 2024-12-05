package io.reflectoring.buckpal.account.domain

import java.time.LocalDateTime

class ActivityWindow {

    private val activities: MutableList<Activity>

    val startTimestamp: LocalDateTime
        get() = activities.minByOrNull { it.timestamp }
            ?.timestamp ?: throw IllegalStateException()

    val endTimestamp: LocalDateTime
        get() = activities.maxByOrNull { it.timestamp }
            ?.timestamp ?: throw IllegalStateException()

    fun calculateBalance(accountId: Account.AccountId): Money {
        val depositBalance = activities
            .filter { it.targetAccountId == accountId }
            .map { it.money }
            .fold(Money.ZERO, Money::plus)

        val withdrawalBalance = activities
            .filter { it.sourceAccountId == accountId }
            .map { it.money }
            .fold(Money.ZERO, Money::plus)

        return depositBalance + withdrawalBalance.negate()
    }

    constructor(activities: List<Activity>) {
        this.activities = activities.toMutableList()
    }

    constructor(vararg activity: Activity) : this(activity.toList())

    fun getActivities(): List<Activity> = activities.toList()

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }
}