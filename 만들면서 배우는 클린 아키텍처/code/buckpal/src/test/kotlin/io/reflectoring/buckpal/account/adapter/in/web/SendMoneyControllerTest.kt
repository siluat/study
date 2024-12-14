package io.reflectoring.buckpal.account.adapter.`in`.web

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyCommand
import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@WebMvcTest(controllers = [SendMoneyController::class])
class SendMoneyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var sendMoneyUseCase: SendMoneyUseCase

    @TestConfiguration
    class TestConfig {
        @Bean
        fun sendMoneyUseCase(): SendMoneyUseCase = mockk(relaxed = true)
    }

    @Test
    fun `test send money endpoint`() {
        val sourceAccountId = 41L
        val targetAccountId = 42L
        val amount = 500L

        every { sendMoneyUseCase.sendMoney(any()) } returns true

        mockMvc.perform(
            post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500)
                .header("Content-Type", "application/json")
        )
            .andExpect(status().isOk)

        verify {
            sendMoneyUseCase.sendMoney(
                SendMoneyCommand(
                    Account.AccountId(sourceAccountId),
                    Account.AccountId(targetAccountId),
                    Money.of(amount)
                )
            )
        }
    }

}