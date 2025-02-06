package malewicz.jakub.notification_service.dtos

import java.util.*

data class ActivateAccountMessage(val token: UUID, val email: String)