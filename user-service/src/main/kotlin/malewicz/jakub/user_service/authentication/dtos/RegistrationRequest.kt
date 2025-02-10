package malewicz.jakub.user_service.authentication.dtos

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import malewicz.jakub.user_service.authentication.validators.Password
import malewicz.jakub.user_service.user.entities.LengthUnits
import malewicz.jakub.user_service.user.entities.WeightUnits
import java.time.LocalDate

data class RegistrationRequest(
    @field:Email
    val email: String,
    @field:Password
    val password: String,
    @field:NotEmpty
    val firstName: String,
    @field:NotEmpty
    val lastName: String,
    val dateOfBirth: LocalDate,
    val weightUnits: WeightUnits,
    val lengthUnits: LengthUnits,
)
