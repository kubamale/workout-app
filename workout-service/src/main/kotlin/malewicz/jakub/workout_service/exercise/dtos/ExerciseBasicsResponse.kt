package malewicz.jakub.workout_service.exercise.dtos

import java.util.*

data class ExerciseBasicsResponse(
    val id: UUID,
    val name: String,
    val pictureURL: String?
)
