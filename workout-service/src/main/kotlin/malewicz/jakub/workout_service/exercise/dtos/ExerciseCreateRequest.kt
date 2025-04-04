package malewicz.jakub.workout_service.exercise.dtos

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import malewicz.jakub.workout_service.set.dtos.SetType
import java.util.UUID

data class ExerciseCreateRequest<T>(
    val workoutId: UUID,
    val exerciseId: UUID,
    val exerciseType: SetType,
    @field:Min(0)
    val order: Int,
    @field:Valid
    val sets: MutableList<T> = mutableListOf(),
)
