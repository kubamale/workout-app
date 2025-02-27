package malewicz.jakub.statistics_service.statistics.services

import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import malewicz.jakub.statistics_service.statistics.dtos.OverallStatistics
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class StatisticsService(
  private val workoutService: WorkoutService,
  private val measurementService: MeasurementService
) {
  fun getOverallStatistics(userId: UUID) = OverallStatistics(
    workoutService.getDaysSinceLastWorkout(userId),
    workoutService.getWorkoutsFromCurrentWeek(userId),
    measurementService.getMeasurementsSince(userId, LocalDateTime.now().minusDays(30))
  )
}