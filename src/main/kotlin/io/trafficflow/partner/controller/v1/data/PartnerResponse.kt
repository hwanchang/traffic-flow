package io.trafficflow.partner.controller.v1.data

import io.trafficflow.partner.domain.Partner

class PartnerResponse(partner: Partner) {
    val id: Long = partner.id

    val name: String = partner.name

    val isOpened: Boolean = partner.isOpened
}
