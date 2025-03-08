package io.trafficflow.partnerconnector.client

import io.trafficflow.configuration.feign.FeignConfiguration
import io.trafficflow.partnerconnector.client.data.ProductFeignResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "partnerconnector-product",
    url = "\${feign.url}",
    configuration = [FeignConfiguration::class],
)
interface ProductFeignClient {
    @GetMapping("api/v1/partners/{partnerId}/products/open")
    fun getAllOpenProducts(@PathVariable partnerId: Long): List<ProductFeignResponse>
}
