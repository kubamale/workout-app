package malewicz.jakub.user_service.authentication.dtos

import jakarta.validation.constraints.Email

data class ForgotPasswordRequest(
    @field:Email
    val email: String,
)