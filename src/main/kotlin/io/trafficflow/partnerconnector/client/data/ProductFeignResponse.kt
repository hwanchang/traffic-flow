package io.trafficflow.partnerconnector.client.data

import io.trafficflow.partnerconnector.domain.Product

class ProductFeignResponse(
    val id: Long,

    val name: String,

    val isOpened: Boolean,

    val partnerId: Long,
) {
    fun toDomain() = Product(id = id, name = name, isOpened = isOpened, partnerId = partnerId)
}
