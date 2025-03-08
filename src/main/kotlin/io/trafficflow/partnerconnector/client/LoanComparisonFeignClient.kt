package io.trafficflow.partnerconnector.client

import io.trafficflow.configuration.feign.FeignConfiguration
import io.trafficflow.partnerconnector.client.data.LoanComparisonFeignResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "partnerconnector-loancomparison",
    url = "\${feign.url}/api/v1/loan-comparisons",
    configuration = [FeignConfiguration::class],
)
interface LoanComparisonFeignClient {
    @GetMapping("{id}")
    fun get(@PathVariable id: Long): LoanComparisonFeignResponse
}
