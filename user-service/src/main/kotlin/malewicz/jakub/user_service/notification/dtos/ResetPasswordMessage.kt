package malewicz.jakub.user_service.notification.dtos

data class ResetPasswordMessage(val token: String, val email: String)
