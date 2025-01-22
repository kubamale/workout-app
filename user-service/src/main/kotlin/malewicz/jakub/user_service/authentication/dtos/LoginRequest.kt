package malewicz.jakub.user_service.authentication.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:Email
    val email: String,
    @field:NotBlank
    val password: String
)
