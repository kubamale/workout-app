package malewicz.jakub.user_service.authentication.dtos

import jakarta.validation.constraints.NotBlank
import malewicz.jakub.user_service.authentication.validators.Password

data class ResetPasswordRequest(
    @field:NotBlank
    val token: String,
    @field:Password
    val password: String
)
