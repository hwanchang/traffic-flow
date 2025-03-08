package io.trafficflow.partner.controller.v1.data

import io.trafficflow.partner.domain.Product

class ProductResponse(product: Product) {
    val id: Long = product.id

    val name: String = product.name

    val isOpened: Boolean = product.isOpened

    val productId: Long = product.partnerId
}
