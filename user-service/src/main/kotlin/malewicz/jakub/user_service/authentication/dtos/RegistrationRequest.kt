package malewicz.jakub.user_service.authentication.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import malewicz.jakub.user_service.user.entities.WeightUnits
import java.time.LocalDate

data class RegistrationRequest(
    @field:Email
    val email: String,
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")
    val password: String,
    @field:NotEmpty
    val firstName: String,
    @field:NotEmpty
    val lastName: String,
    val dateOfBirth: LocalDate,
    val weightUnits: WeightUnits
)
