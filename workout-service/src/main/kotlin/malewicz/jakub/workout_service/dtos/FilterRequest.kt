package malewicz.jakub.workout_service.dtos

data class FilterRequest(
    val value: Any,
    val field: String,
)
