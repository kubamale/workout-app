package malewicz.jakub.user_service.notification.services

import malewicz.jakub.user_service.notification.dtos.ActivateAccountMessage
import malewicz.jakub.user_service.notification.dtos.ResetPasswordMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class NotificationService(
    private val kafkaTemplate: KafkaTemplate<Int, Any>,
    @Value("\${kafka.topics.activateAccount}") private val activateAccountTopic: String,
    @Value("\${kafka.topics.resetPassword}") private val resetPasswordTopic: String
) {

    fun sendActivateAccountEmail(userId: UUID, email: String) {
        kafkaTemplate.send(activateAccountTopic, ActivateAccountMessage(userId, email))
    }

    fun sendResetPasswordEmail(token: String, email: String) {
        kafkaTemplate.send(resetPasswordTopic, ResetPasswordMessage(token, email))
    }
}