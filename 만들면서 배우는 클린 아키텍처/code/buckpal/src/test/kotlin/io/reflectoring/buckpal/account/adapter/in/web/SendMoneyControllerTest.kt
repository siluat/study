package io.reflectoring.buckpal.account.adapter.`in`.web

import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyCommand
import io.reflectoring.buckpal.account.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.account.domain.Account
import io.reflectoring.buckpal.account.domain.Money
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.then

@WebMvcTest(controllers = [SendMoneyController::class])
class SendMoneyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var sendMoneyUseCase: SendMoneyUseCase

    @Test
    fun `test send money endpoint`() {
        mockMvc.perform(
            post("/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}", 41L, 42L, 500)
                .header("Content-Type", "application/json")
        )
            .andExpect(status().isOk)

        then(sendMoneyUseCase).should().sendMoney(
            eq(
                SendMoneyCommand(
                    Account.AccountId(41L),
                    Account.AccountId(42L),
                    Money.of(500L)
                )
            )
        )
    }
}