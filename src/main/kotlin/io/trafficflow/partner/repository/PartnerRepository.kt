package io.trafficflow.partner.repository

import io.trafficflow.partner.domain.Partner
import org.springframework.data.jpa.repository.JpaRepository

interface PartnerRepository : JpaRepository<Partner, Long> {
    override fun findAll(): List<Partner>

    fun findAllByIsOpened(opened: Boolean): List<Partner>
}
