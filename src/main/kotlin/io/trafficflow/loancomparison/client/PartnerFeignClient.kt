package io.trafficflow.loancomparison.client

import io.trafficflow.configuration.feign.FeignConfiguration
import io.trafficflow.loancomparison.client.data.PartnerFeignResponse
import io.trafficflow.loancomparison.client.data.ProductFeignResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "loancomparison-partner",
    url = "\${feign.url}/api/v1/partners",
    configuration = [FeignConfiguration::class],
)
interface PartnerFeignClient {
    @GetMapping("open")
    fun getAllOpenPartners(): List<PartnerFeignResponse>

    @GetMapping("/{productId}/products/open")
    fun getAllOpenProducts(@PathVariable productId: Long): List<ProductFeignResponse>
}
