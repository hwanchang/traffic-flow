package io.trafficflow.configuration.kafka

import org.apache.kafka.clients.CommonClientConfigs.SECURITY_PROTOCOL_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer.VALUE_DEFAULT_TYPE
import org.springframework.kafka.support.serializer.JsonSerializer
import org.springframework.util.backoff.FixedBackOff

@Configuration
@Profile("local")
class KafkaLocalConfiguration(
    private val kafkaProperties: KafkaProperties,
) {
    @Bean
    fun kafkaMessageKafkaTemplate() = KafkaTemplate(kafkaMessageProducerFactory())

    @Bean
    fun kafkaMessageProducerFactory(): ProducerFactory<String, KafkaMessage> {
        return DefaultKafkaProducerFactory(
            mapOf(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                SECURITY_PROTOCOL_CONFIG to kafkaProperties.security.protocol,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            )
        )
    }

    @Bean
    fun kafkaMessageNoRetryContainerFactory() = ConcurrentKafkaListenerContainerFactory<String, KafkaMessage>().apply {
        consumerFactory = DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                SECURITY_PROTOCOL_CONFIG to kafkaProperties.security.protocol,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
                VALUE_DEFAULT_TYPE to KafkaMessage::class.java,
            ),
        )
    }

    @Bean
    fun kafkaMessageContainerFactory() = ConcurrentKafkaListenerContainerFactory<String, KafkaMessage>().apply {
        consumerFactory = DefaultKafkaConsumerFactory(
            mapOf(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                SECURITY_PROTOCOL_CONFIG to kafkaProperties.security.protocol,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
                VALUE_DEFAULT_TYPE to KafkaMessage::class.java,
            )
        )
        setCommonErrorHandler(
            DefaultErrorHandler(
                DeadLetterPublishingRecoverer(kafkaMessageKafkaTemplate()),
                FixedBackOff(1000L, 3L),
            ),
        )
    }
}
