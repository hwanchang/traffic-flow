package io.trafficflow.partner.service

import io.trafficflow.partner.domain.Partner
import io.trafficflow.partner.repository.PartnerRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PartnerService(
    private val partnerRepository: PartnerRepository,
) {
    @Transactional
    fun add(partner: Partner) {
        partnerRepository.save(partner)
    }

    @Transactional(readOnly = true)
    fun getAll() = partnerRepository.findAll()

    @Transactional(readOnly = true)
    fun getAllPartnersByOpened(isOpened: Boolean) = partnerRepository.findAllByIsOpened(isOpened)

    @Transactional(readOnly = true)
    fun get(id: Long) = partnerRepository.findByIdOrNull(id) ?: throw EntityNotFoundException()
}
