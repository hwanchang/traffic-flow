package io.trafficflow.configuration.feign

import feign.RequestInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FeignConfiguration {
    @Bean
    fun requestInterceptor(@Value("\${internal-api-key}") internalApiKey: String) =
        RequestInterceptor { it.header("X-API-KEY", internalApiKey) }
}
