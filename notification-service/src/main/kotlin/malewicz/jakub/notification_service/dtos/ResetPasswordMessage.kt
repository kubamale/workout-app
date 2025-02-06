package malewicz.jakub.notification_service.dtos

data class ResetPasswordMessage(val token: String, val email: String)
