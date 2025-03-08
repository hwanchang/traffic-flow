package io.trafficflow.loancomparison.service

import io.trafficflow.configuration.kafka.KafkaMessage
import io.trafficflow.loancomparison.client.PartnerFeignClient
import io.trafficflow.loancomparison.client.data.PartnerFeignResponse
import io.trafficflow.loancomparison.client.data.ProductFeignResponse
import io.trafficflow.loancomparison.domain.LoanComparison
import io.trafficflow.loancomparison.repository.LoanComparisonRepository
import jakarta.persistence.EntityNotFoundException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoanComparisonService(
    private val loanComparisonRepository: LoanComparisonRepository,

    private val partnerFeignClient: PartnerFeignClient,

    private val kafkaMessageKafkaTemplate: KafkaTemplate<String, KafkaMessage>,

    @Value("\${kafka.topic.loan-comparison}")
    private val loanComparisonTopic: String,
) {
    fun compare(userId: Long) = partnerFeignClient.getAllOpenPartners()
        .asSequence()
        .map(PartnerFeignResponse::toDomain)
        .flatMap { partnerFeignClient.getAllOpenProducts(it.id) }
        .map(ProductFeignResponse::toDomain)
        .map { LoanComparison(userId = userId, partnerId = it.id, productId = it.productId) }
        .onEach(loanComparisonRepository::save)
        .map { KafkaMessage(body = it.id) }
        .forEach { kafkaMessageKafkaTemplate.send(loanComparisonTopic, it) }

    @Transactional(readOnly = true)
    fun get(id: Long) = loanComparisonRepository.findByIdOrNull(id) ?: throw EntityNotFoundException()

    @Transactional
    fun accepted(id: Long) {
        runBlocking {
            withContext(IO) {
                loanComparisonRepository.findByIdOrNull(id)
                    ?.also {
                        it.accepted()
                        loanComparisonRepository.save(it)
                    }
                    ?: throw EntityNotFoundException("${id}: 대출비교가 없습니다.")
            }
        }
    }

    @Transactional
    fun rejected(id: Long) {
        runBlocking {
            withContext(IO) {
                loanComparisonRepository.findByIdOrNull(id)
                    ?.also {
                        it.rejected()
                        loanComparisonRepository.save(it)
                    }
                    ?: throw EntityNotFoundException("${id}: 대출비교가 없습니다.")
            }
        }
    }
}
