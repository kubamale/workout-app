package malewicz.jakub.workout_service.exercise.dtos

import java.util.UUID

data class ExerciseReorderRequest(
    val  workoutId: UUID,
    val exerciseOrder: Map<UUID, Int>,
) {
}