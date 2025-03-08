package io.trafficflow.partner.domain

import io.trafficflow.common.entity.BaseTimeEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import org.hibernate.annotations.DynamicUpdate

@DynamicUpdate
@Entity
class Product(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    val name: String,

    val isOpened: Boolean,

    val partnerId: Long,
) : BaseTimeEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
