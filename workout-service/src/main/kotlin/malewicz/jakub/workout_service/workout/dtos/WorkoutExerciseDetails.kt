package malewicz.jakub.workout_service.workout.dtos

import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.set.dtos.SetResponse
import java.util.*

data class WorkoutExerciseDetails(
    val id: UUID,
    val exercise: ExerciseDetails,
    val order: Int,
    val sets: MutableList<SetResponse>,
)