package malewicz.jakub.user_service.notification.dtos

import java.util.UUID

data class ActivateAccountMessage(val token: UUID, val email: String)
