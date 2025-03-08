package io.trafficflow.partner.controller.v1.data

import io.trafficflow.partner.domain.Product

data class AddProductRequest(
    val name: String,

    val isOpened: Boolean,
) {
    fun toDomain(partnerId: Long) = Product(name = name, isOpened = isOpened, partnerId = partnerId)
}
