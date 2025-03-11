package malewicz.jakub.statistics_service.exercises.dtos

import malewicz.jakub.statistics_service.sets.dtos.SetDto
import malewicz.jakub.statistics_service.sets.dtos.SetType
import java.util.UUID

data class ExerciseStatisticsDto(
  val exerciseId: UUID,
  val workoutExerciseId: UUID,
  val type: SetType,
  val sets: MutableList<SetDto> = mutableListOf(),
)
