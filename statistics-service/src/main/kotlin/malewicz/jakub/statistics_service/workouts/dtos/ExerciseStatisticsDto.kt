package malewicz.jakub.statistics_service.workouts.dtos

import java.util.UUID

data class ExerciseStatisticsDto(
    val exerciseId: UUID,
    val workoutExerciseId: UUID,
    val type: SetType,
    val sets: MutableList<SetDto> = mutableListOf(),
)
