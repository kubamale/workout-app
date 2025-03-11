package malewicz.jakub.statistics_service.statistics.dtos

import java.time.LocalDateTime
import java.util.*

data class ExerciseStatistics(
  val exerciseId: UUID,
  val date: LocalDateTime,
  val volume: Double? = null,
  val oneRepMax: Double? = null,
  val maxWeight: Double? = null,
  val reps: Int? = null,
  val time: Long? = null,
  val distance: Double? = null,
)