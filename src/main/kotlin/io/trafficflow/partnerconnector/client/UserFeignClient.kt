package io.trafficflow.partnerconnector.client

import io.trafficflow.configuration.feign.FeignConfiguration
import io.trafficflow.partnerconnector.client.data.UserFeignResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    name = "partnerconnector-user",
    url = "\${feign.url}/api/v1/users",
    configuration = [FeignConfiguration::class],
)
interface UserFeignClient {
    @GetMapping("{id}")
    fun get(@PathVariable id: Long): UserFeignResponse
}
