package malewicz.jakub.statistics_service.workouts.dtos

import malewicz.jakub.statistics_service.exercises.dtos.ExerciseStatisticsDto
import java.util.UUID

data class WorkoutCreateRequest(
    val workoutId: UUID,
    val exercises: List<ExerciseStatisticsDto> = mutableListOf()
)
