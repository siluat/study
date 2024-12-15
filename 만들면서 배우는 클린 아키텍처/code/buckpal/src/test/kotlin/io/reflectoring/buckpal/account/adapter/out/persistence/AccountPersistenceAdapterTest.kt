package io.reflectoring.buckpal.account.adapter.out.persistence

import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.ActivityWindow
import io.reflectoring.buckpal.account.domain.Money
import io.reflectoring.buckpal.common.AccountTestData.defaultAccount
import io.reflectoring.buckpal.common.ActivityTestData.defaultActivity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

@DataJpaTest
@Import(AccountPersistenceAdapter::class, AccountMapper::class)
class AccountPersistenceAdapterTest {

    @Autowired
    lateinit var adapterUnderTest: AccountPersistenceAdapter

    @Autowired
    lateinit var activityRepository: ActivityRepository

    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    fun loadsAccount() {
        val account = adapterUnderTest.loadAccount(Account.AccountId(1L), LocalDateTime.of(2018, 8, 10, 0, 0))

        assertEquals(account.activityWindow.getActivities().size, 2)
        assertEquals(account.calculateBalance(), Money.of(500))
    }

    @Test
    fun updatesActivities() {
        val account = defaultAccount()
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                ActivityWindow(
                    listOf(
                        defaultActivity()
                            .withMoney(Money.of(1L))
                            .build()
                    )
                )
            ).build()

        adapterUnderTest.updateActivities(account)

        assertEquals(activityRepository.count(), 1)

        val savedActivity = activityRepository.findAll()[0]
        assertEquals(savedActivity.amount, 1L);
    }

}
