package io.reflectoring.buckpal

import io.reflectoring.buckpal.account.application.service.MoneyTransferProperties
import io.reflectoring.buckpal.account.domain.Money
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(BuckpalConfigurationProperties::class)
class BuckpalConfiguration {

    @Bean
    fun moneyTransferProperties(buckpalConfigurationProperties: BuckpalConfigurationProperties): MoneyTransferProperties {
        return MoneyTransferProperties(Money.of(buckpalConfigurationProperties.transferThreshold))
    }

}
