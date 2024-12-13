package io.reflectoring.buckpal

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "buckpal")
data class BuckpalConfigurationProperties(
    var transferThreshold: Long = Long.MAX_VALUE
)
