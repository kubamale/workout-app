package malewicz.jakub.exercise_service.dtos

import java.util.*

data class ExerciseBasicsResponse(
    val id: UUID,
    val name: String,
    val pictureURL: String?
)
