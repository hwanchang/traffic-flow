package io.trafficflow.loancomparison.client.data

import io.trafficflow.loancomparison.domain.Partner

class PartnerFeignResponse(
    val id: Long,

    val name: String,

    val isOpened: Boolean,
) {
    fun toDomain() = Partner(id = id, name = name, isOpened = isOpened)
}
