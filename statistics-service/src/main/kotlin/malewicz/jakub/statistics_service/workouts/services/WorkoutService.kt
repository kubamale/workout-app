package malewicz.jakub.statistics_service.workouts.services

import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.mappers.WorkoutMapper
import malewicz.jakub.statistics_service.workouts.repositories.WorkoutRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkoutService(private val workoutRepository: WorkoutRepository, private val workoutMapper: WorkoutMapper) {
    fun createWorkout(workoutCreateRequest: WorkoutCreateRequest, userId: UUID) {
        val workout = workoutMapper.toUserWorkoutEntity(workoutCreateRequest, userId)
        workoutRepository.save(workout)
    }
}