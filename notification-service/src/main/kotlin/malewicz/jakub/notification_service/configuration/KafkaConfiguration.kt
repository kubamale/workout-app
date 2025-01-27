package malewicz.jakub.notification_service.configuration

import malewicz.jakub.notification_service.listeners.ActivateAccountMessage
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.IntegerDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer


@Configuration
class KafkaConfiguration {

    @Bean
    fun consumerFactory(): ConsumerFactory<Int, ActivateAccountMessage> {
        val jsonDeserializer = JsonDeserializer(ActivateAccountMessage::class.java)
        return DefaultKafkaConsumerFactory(
            factoryConfig(), IntegerDeserializer(),
            jsonDeserializer
        )
    }

    @Bean
    fun accountActivationListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, ActivateAccountMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, ActivateAccountMessage>()
        factory.consumerFactory = consumerFactory()
        return factory
    }

    private fun factoryConfig(): Map<String, Any> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = "127.0.0.1:9092"
        props[ConsumerConfig.GROUP_ID_CONFIG] = "id"
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = IntegerDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        return props
    }

}