package malewicz.jakub.api_gateway.models

import java.util.UUID

data class UserData(
    val id: UUID,
    val email: String,
    val weightUnits: WeightUnits,
    val lengthUnits: LengthUnits,
)
