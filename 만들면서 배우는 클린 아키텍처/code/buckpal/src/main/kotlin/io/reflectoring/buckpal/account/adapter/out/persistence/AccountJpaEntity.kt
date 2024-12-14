package io.reflectoring.buckpal.account.adapter.out.persistence

import jakarta.persistence.*

@Entity
@Table(name = "account")
data class AccountJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
)
