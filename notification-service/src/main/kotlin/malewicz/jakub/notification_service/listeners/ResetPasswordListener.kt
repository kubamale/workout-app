package malewicz.jakub.notification_service.listeners

import malewicz.jakub.notification_service.dtos.ResetPasswordMessage
import malewicz.jakub.notification_service.services.EmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class ResetPasswordListener(
    private val emailService: EmailService,
    @Value("\${gymapp.resetPassword.url}") private val resetPasswordUrl: String
) {
    @KafkaListener(
        topics = ["\${kafka.topic.resetPassword}"],
        groupId = "test",
        containerFactory = "resetPasswordListenerContainerFactory"
    )
    fun listen(message: ResetPasswordMessage) {
        emailService.send(
            "ResetPasswordEmail",
            message.email,
            "Reset Password",
            mapOf(Pair("resetPasswordUrl", "$resetPasswordUrl/${message.token}"))
        )
    }
}