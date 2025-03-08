package io.trafficflow.loancomparison.client.data

import io.trafficflow.loancomparison.domain.Product

class ProductFeignResponse(
    val id: Long,

    val name: String,

    val isOpened: Boolean,

    val productId: Long,
) {
    fun toDomain() = Product(id = id, name = name, isOpened = isOpened, productId = productId)
}
