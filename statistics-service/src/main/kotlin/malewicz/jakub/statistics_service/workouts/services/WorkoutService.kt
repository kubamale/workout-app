package malewicz.jakub.statistics_service.workouts.services

import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.mappers.WorkoutMapper
import malewicz.jakub.statistics_service.workouts.repositories.WorkoutRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class WorkoutService(private val workoutRepository: WorkoutRepository, private val workoutMapper: WorkoutMapper) {
  fun createWorkout(workoutCreateRequest: WorkoutCreateRequest, userId: UUID) {
    val workout = workoutMapper.toUserWorkoutEntity(workoutCreateRequest, userId)
    workoutRepository.save(workout)
  }

  fun getDaysSinceLastWorkout(userId: UUID) =
    workoutRepository.getLatestWorkoutDate(userId)?.let { ChronoUnit.DAYS.between(LocalDate.now(), it) }

  fun getWorkoutsFromCurrentWeek(userId: UUID) {
    val today = LocalDate.now()
    val dayOfWeek = today.getDayOfWeek() - 1
    val weekStart = today.minusDays(dayOfWeek.value.toLong())
    val usersWorkouts = workoutRepository.getAllByUserIdAndDateGreaterThan(userId, weekStart.atTime(0,0,0))
  }
}