package malewicz.jakub.workout_service.workout.dtos

import java.util.*

data class WorkoutDetailsResponse(
    val id: UUID,
    val name: String,
    val exercises: MutableList<WorkoutExerciseDetails>,
)
