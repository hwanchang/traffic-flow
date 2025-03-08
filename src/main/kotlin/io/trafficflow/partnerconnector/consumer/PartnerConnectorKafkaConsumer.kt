package io.trafficflow.partnerconnector.consumer

import io.trafficflow.common.event.EventFailover
import io.trafficflow.common.event.EventFailoverHandler
import io.trafficflow.configuration.kafka.KafkaMessage
import io.trafficflow.partnerconnector.service.PartnerConnectorService
import io.trafficflow.partnerconnector.service.RateLimiter
import java.time.LocalDateTime.now
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders.DLT_EXCEPTION_MESSAGE
import org.springframework.kafka.support.KafkaHeaders.DLT_EXCEPTION_STACKTRACE
import org.springframework.kafka.support.KafkaHeaders.DLT_ORIGINAL_TOPIC
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class PartnerConnectorKafkaConsumer(
    private val rateLimiter: RateLimiter,

    private val partnerConnectorService: PartnerConnectorService,

    private val eventFailoverHandler: EventFailoverHandler,
) {
    @KafkaListener(
        topics = ["\${kafka.topic.loan-comparison}"],
        groupId = "\${kafka.group.loan-comparison}",
        containerFactory = "kafkaMessageContainerFactory",
    )
    fun compare(@Payload kafkaMessage: KafkaMessage) {
        if (rateLimiter.isLimited(kafkaMessage)) {
            return
        }

        partnerConnectorService.compare(kafkaMessage.asBody<Long>())
    }

    @KafkaListener(
        topics = ["\${kafka.topic.loan-comparison}-dlt"],
        groupId = "\${kafka.group.loan-comparison}-dlt",
        containerFactory = "kafkaMessageNoRetryContainerFactory",
    )
    fun compareDlt(
        @Header(DLT_ORIGINAL_TOPIC) originalTopic: String,
        @Header(DLT_EXCEPTION_MESSAGE) exceptionMessage: String,
        @Header(DLT_EXCEPTION_STACKTRACE) exceptionStackTrace: String,
        @Payload kafkaMessage: KafkaMessage,
    ) {
        eventFailoverHandler.add(
            EventFailover(
                topic = originalTopic,
                message = kafkaMessage.asBody(),
                exceptionMessage = exceptionMessage,
                exceptionStacktrace = exceptionStackTrace,
                failoverCount = kafkaMessage.failoverCount,
                lastRetriedAt = kafkaMessage.lastRetriedAt,
                createdAt = now(),
            )
        )
    }
}
