package io.reflectoring.buckpal.common

import org.springframework.core.annotation.AliasFor
import org.springframework.stereotype.Component

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Component
annotation class PersistenceAdapter(
    @get:AliasFor(annotation = Component::class, attribute = "value")
    val value: String = ""
)
