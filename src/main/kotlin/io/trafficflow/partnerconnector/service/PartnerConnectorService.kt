package io.trafficflow.partnerconnector.service

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.trafficflow.partnerconnector.client.BankFeignClient
import io.trafficflow.partnerconnector.client.LoanComparisonFeignClient
import io.trafficflow.partnerconnector.client.UserFeignClient
import io.trafficflow.partnerconnector.client.data.CompareFeignRequest
import io.trafficflow.partnerconnector.client.data.UserFeignResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PartnerConnectorService(
    private val circuitBreakerRegistry: CircuitBreakerRegistry,

    private val loanComparisonFeignClient: LoanComparisonFeignClient,

    private val userFeignClient: UserFeignClient,

    private val bankFeignClient: BankFeignClient,
) {
    @Transactional
    fun compare(id: Long) {
        val loanComparison = loanComparisonFeignClient.get(id).toDomain()
        val user = userFeignClient.get(id = loanComparison.userId).let(UserFeignResponse::toDomain)

        try {
            circuitBreakerRegistry.circuitBreaker("partner-connector").executeSupplier {
                bankFeignClient.compare(
                    CompareFeignRequest(
                        id = loanComparison.id,
                        userEmail = user.email,
                        partnerId = loanComparison.partnerId,
                        productId = loanComparison.productId,
                    )
                )
            }
        } catch (exception: Exception) {
            throw exception
        }
    }
}
