package malewicz.jakub.statistics_service.statistics.services

import malewicz.jakub.statistics_service.exercises.services.ExerciseService
import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import malewicz.jakub.statistics_service.statistics.dtos.OverallStatistics
import malewicz.jakub.statistics_service.sets.dtos.SetType
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class StatisticsService(
  private val workoutService: WorkoutService,
  private val measurementService: MeasurementService,
  private val exerciseService: ExerciseService
) {
  fun getOverallStatistics(userId: UUID) = OverallStatistics(
    workoutService.getDaysSinceLastWorkout(userId),
    workoutService.getWorkoutsFromCurrentWeek(userId),
    measurementService.getMeasurementsSince(userId, LocalDateTime.now().minusDays(30))
  )

  fun getExerciseStatistics(exerciseId: UUID, userId: UUID): Map<SetType, List<ExerciseStatistics>> {
    val exercises = exerciseService.getExercisesForUser(userId, exerciseId)
    return exerciseService.getStatisticsForExercises(exercises)
  }
}