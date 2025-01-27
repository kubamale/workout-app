package malewicz.jakub.notification_service.listeners

import malewicz.jakub.notification_service.services.EmailService
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.*

@Component
class AccountCreationListener(
    private val emailService: EmailService,
    @Value("\${gymapp.activationUrl}") private val activationUrl: String
) {
    @KafkaListener(
        topics = ["\${kafka.topic.activateAccount}"],
        groupId = "test-group-id",
        containerFactory = "accountActivationListenerContainerFactory"
    )
    fun listen(data: ActivateAccountMessage) {
        val map = hashMapOf(Pair("activationUrl", "$activationUrl/${data.token}"))
        emailService.send("ActivateAccountEmail", data.email, map)
    }
}

data class ActivateAccountMessage(val token: UUID, val email: String)