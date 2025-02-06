package malewicz.jakub.notification_service.configuration

import malewicz.jakub.notification_service.dtos.ActivateAccountMessage
import malewicz.jakub.notification_service.dtos.ResetPasswordMessage
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
    fun activateAccountConsumerFactory(): ConsumerFactory<Int, ActivateAccountMessage> {
        val jsonDeserializer = JsonDeserializer(ActivateAccountMessage::class.java)
        return DefaultKafkaConsumerFactory(
            factoryConfig(), IntegerDeserializer(),
            jsonDeserializer
        )
    }

    @Bean
    fun accountActivationListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, ActivateAccountMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, ActivateAccountMessage>()
        factory.consumerFactory = activateAccountConsumerFactory()
        return factory
    }

    @Bean
    fun resetPasswordConsumerFactory(): ConsumerFactory<Int, ResetPasswordMessage> {
        val jsonDeserializer = JsonDeserializer(ResetPasswordMessage::class.java)
        return DefaultKafkaConsumerFactory(
            factoryConfig(), IntegerDeserializer(),
            jsonDeserializer
        )
    }

    @Bean
    fun resetPasswordListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<Int, ResetPasswordMessage> {
        val factory = ConcurrentKafkaListenerContainerFactory<Int, ResetPasswordMessage>()
        factory.consumerFactory = resetPasswordConsumerFactory()
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