package io.trafficflow.loancomparison.domain

import io.trafficflow.common.entity.BaseTimeEntity
import io.trafficflow.loancomparison.domain.Status.ACCEPTED
import io.trafficflow.loancomparison.domain.Status.PENDING
import io.trafficflow.loancomparison.domain.Status.REJECTED
import jakarta.persistence.Entity
import jakarta.persistence.EnumType.STRING
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import org.hibernate.annotations.DynamicUpdate


@DynamicUpdate
@Entity
class LoanComparison(
    @Id
    @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0,

    val userId: Long,

    val partnerId: Long,

    val productId: Long,

    @Enumerated(STRING)
    var status: Status = PENDING,
) : BaseTimeEntity() {
    fun accepted() {
        status = ACCEPTED
    }

    fun rejected() {
        status = REJECTED
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoanComparison

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
