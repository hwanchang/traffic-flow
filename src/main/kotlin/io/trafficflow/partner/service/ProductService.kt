package io.trafficflow.partner.service

import io.trafficflow.partner.domain.Product
import io.trafficflow.partner.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    @Transactional
    fun add(product: Product) {
        productRepository.save(product)
    }

    @Transactional(readOnly = true)
    fun getAllByPartnerIdAndOpened(partnerId: Long, isOpened: Boolean) =
        productRepository.findAllByPartnerIdAndIsOpened(partnerId = partnerId, isOpened = isOpened)

    @Transactional(readOnly = true)
    fun get(partnerId: Long, productId: Long) =
        productRepository.findByIdAndPartnerId(id = productId, partnerId = partnerId)
}
