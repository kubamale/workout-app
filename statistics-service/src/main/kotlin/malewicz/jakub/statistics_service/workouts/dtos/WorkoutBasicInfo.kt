package malewicz.jakub.statistics_service.workouts.dtos

import java.util.UUID

data class WorkoutBasicInfo(
  val id: UUID,
  val name: String,
)
