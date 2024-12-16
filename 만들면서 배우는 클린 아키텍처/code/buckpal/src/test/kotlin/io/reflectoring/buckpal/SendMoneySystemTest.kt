package io.reflectoring.buckpal

import io.reflectoring.buckpal.account.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.Money
import org.assertj.core.api.BDDAssertions.then
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var loadAccountPort: LoadAccountPort

    @Test
    @Sql("SendMoneySystemTest.sql")
    fun sendMoney() {
        val initialSourceBalance = sourceAccount().calculateBalance()
        val initialTargetBalance = targetAccount().calculateBalance()

        val response = whenSendMoney(sourceAccountId(), targetAccountId(), transferredAmount())

        assertEquals(response.statusCode, HttpStatus.OK)

        assertEquals(
            expected = initialSourceBalance.minus(transferredAmount()),
            actual = sourceAccount().calculateBalance(),
        )

        assertEquals(
            expected = initialTargetBalance.plus(transferredAmount()),
            actual = targetAccount().calculateBalance(),
        )
    }

    private fun sourceAccount(): Account {
        return loadAccount(sourceAccountId())
    }

    private fun targetAccount(): Account {
        return loadAccount(targetAccountId())
    }

    private fun loadAccount(accountId: Account.AccountId): Account {
        return loadAccountPort.loadAccount(accountId, LocalDateTime.now())
    }

    private fun whenSendMoney(
        sourceAccountId: Account.AccountId,
        targetAccountId: Account.AccountId,
        amount: Money
    ): ResponseEntity<Any> {
        val headers = HttpHeaders().apply { add("Content-Type", "application/json") }
        val request = HttpEntity<Void>(null, headers)

        return restTemplate.exchange(
            "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
            HttpMethod.POST,
            request,
            Any::class.java,
            sourceAccountId.value,
            targetAccountId.value,
            amount.amount
        )
    }

    private fun transferredAmount(): Money {
        return Money.of(500L)
    }

    private fun sourceAccountId(): Account.AccountId {
        return Account.AccountId(1L)
    }

    private fun targetAccountId(): Account.AccountId {
        return Account.AccountId(2L)
    }

}
