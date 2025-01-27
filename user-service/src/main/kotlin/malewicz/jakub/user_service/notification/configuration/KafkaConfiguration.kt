package malewicz.jakub.user_service.notification.configuration

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaAdmin.NewTopics
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer


@Configuration
class KafkaConfiguration(
    @Value("\${kafka.topics.resetPassword}") private val resetPasswordTopic: String,
    @Value("\${kafka.topics.activateAccount}") private val activateAccountTopic: String
) {

    @Bean
    fun notificationTopics() =
        NewTopics(TopicBuilder.name(resetPasswordTopic).build(), TopicBuilder.name(activateAccountTopic).build())

    @Bean
    fun producerFactory(): ProducerFactory<Int, Any> {
        return DefaultKafkaProducerFactory(producerConfigs())
    }

    @Bean
    fun producerConfigs(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = "localhost:9092"
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = IntegerSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        props[JsonSerializer.ADD_TYPE_INFO_HEADERS] = false
        return props
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<Int, Any> {
        return KafkaTemplate(producerFactory())
    }
}