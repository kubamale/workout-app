package malewicz.jakub.workout_service.set.dtos

import jakarta.validation.constraints.Min
import java.util.*

data class TimeSetCreateRequest(
    val workoutExerciseId: UUID? = null,
    @field:Min(0)
    val setNumber: Int,
    val time: Long,
    @field:Min(0)
    val weight: Double
)
