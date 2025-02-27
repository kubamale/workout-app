package malewicz.jakub.statistics_service.statistics.dtos

import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo

data class OverallStatistics(
  val daysSinceWorkout: Long,
  val thisWeeksWorkouts: List<WorkoutBasicInfo>,
  val thirtyDaysMeasurements: List<MeasurementDetails>
)
