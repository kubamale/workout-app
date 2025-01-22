package malewicz.jakub.workout_service.workout.services

import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkoutService(private val workoutRepository: WorkoutRepository) {

    fun createWorkout(workoutCreateRequest: WorkoutCreateRequest, userId: UUID): UUID {
        val workout = workoutRepository.save(WorkoutEntity(null, workoutCreateRequest.name, userId))
        return workout.id!!
    }
}