package io.trafficflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableFeignClients
@EnableJpaAuditing
@SpringBootApplication
class TrafficFlowApplication

fun main(args: Array<String>) {
    runApplication<TrafficFlowApplication>(*args)
}
