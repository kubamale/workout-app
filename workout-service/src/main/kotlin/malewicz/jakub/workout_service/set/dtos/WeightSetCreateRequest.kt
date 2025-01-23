package malewicz.jakub.workout_service.set.dtos

import jakarta.validation.constraints.Min
import java.util.*

data class WeightSetCreateRequest(
    val workoutExerciseId: UUID? = null,
    @field:Min(0)
    val setNumber: Int,
    @field:Min(0)
    val reps: Int,
    @field:Min(0)
    val weight: Double
)