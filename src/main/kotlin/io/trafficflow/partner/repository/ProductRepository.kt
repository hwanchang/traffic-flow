package io.trafficflow.partner.repository

import io.trafficflow.partner.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Long> {
    fun findAllByPartnerIdAndIsOpened(partnerId: Long, isOpened: Boolean): List<Product>

    fun findByIdAndPartnerId(id: Long, partnerId: Long): Product
}
