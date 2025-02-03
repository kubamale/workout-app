package malewicz.jakub.workout_service.set.dtos

import jakarta.validation.constraints.Min
import java.util.*

data class DistanceSetCreateRequest(
    val workoutExerciseId: UUID? = null,
    @field:Min(0)
    val setNumber: Int,
    val distance: Double,
)

data class TimeSetCreateRequest(
    val workoutExerciseId: UUID? = null,
    @field:Min(0)
    val setNumber: Int,
    val time: Long,
    @field:Min(0)
    var weight: Double
)


data class WeightSetCreateRequest(
    val workoutExerciseId: UUID? = null,
    @field:Min(0)
    val setNumber: Int,
    @field:Min(0)
    val reps: Int,
    @field:Min(0)
    var weight: Double
)