package malewicz.jakub.statistics_service.workouts.dtos

import java.util.UUID

data class WorkoutCreateRequest(
    val workoutId: UUID,
    val exercises: List<ExerciseStatisticsDto> = mutableListOf()
)
