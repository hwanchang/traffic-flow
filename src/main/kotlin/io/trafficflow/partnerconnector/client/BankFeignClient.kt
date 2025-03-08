package io.trafficflow.partnerconnector.client

import io.trafficflow.configuration.feign.FeignConfiguration
import io.trafficflow.partnerconnector.client.data.CompareFeignRequest
import io.trafficflow.partnerconnector.client.data.CompareFeignResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "partnerconnector-bank",
    url = "http://localhost:8081/api/v1/banks",
    configuration = [FeignConfiguration::class],
)
interface BankFeignClient {
    @PostMapping
    fun compare(@RequestBody request: CompareFeignRequest): CompareFeignResponse
}
