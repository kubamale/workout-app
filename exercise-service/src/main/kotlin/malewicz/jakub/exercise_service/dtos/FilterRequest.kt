package malewicz.jakub.exercise_service.dtos

data class FilterRequest(
    val value: Any,
    val field: String,
)
