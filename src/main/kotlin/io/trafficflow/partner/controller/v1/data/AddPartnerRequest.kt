package io.trafficflow.partner.controller.v1.data

import io.trafficflow.partner.domain.Partner

data class AddPartnerRequest(
    val name: String,

    val isOpened: Boolean,
) {
    fun toDomain() = Partner(name = name, isOpened = isOpened)
}
