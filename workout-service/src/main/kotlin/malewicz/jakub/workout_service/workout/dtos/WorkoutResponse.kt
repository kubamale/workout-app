package malewicz.jakub.workout_service.workout.dtos

import java.util.UUID

data class WorkoutResponse(
    val id: UUID,
    val name: String,
)
