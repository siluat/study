package io.reflectoring.buckpal.account.adapter.out.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "activity")
data class ActivityJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    val timestamp: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val ownerAccountId: Long,

    @Column(nullable = false)
    val sourceAccountId: Long,

    @Column(nullable = false)
    val targetAccountId: Long,

    @Column(nullable = false)
    val amount: Long
)
