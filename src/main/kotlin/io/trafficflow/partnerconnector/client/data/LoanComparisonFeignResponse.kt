package io.trafficflow.partnerconnector.client.data

import io.trafficflow.loancomparison.domain.Status
import io.trafficflow.partnerconnector.domain.LoanComparison

class LoanComparisonFeignResponse(
    val id: Long,

    val userId: Long,

    val partnerId: Long,

    val productId: Long,

    var status: Status,
) {
    fun toDomain() = LoanComparison(id = id, userId = userId, partnerId = partnerId, productId = productId)
}
