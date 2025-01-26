package malewicz.jakub.workout_service.workout.services

import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.mappers.WorkoutMapper
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkoutService(
    private val workoutRepository: WorkoutRepository,
    private val workoutMapper: WorkoutMapper,
    private val workoutExerciseRepository: WorkoutExerciseRepository,
) {

    fun createWorkout(workoutCreateRequest: WorkoutCreateRequest, userId: UUID): UUID {
        val workout = workoutRepository.save(WorkoutEntity(null, workoutCreateRequest.name, userId))
        return workout.id!!
    }

    fun getUserWorkouts(userId: UUID) =
        workoutRepository.findByUserId(userId).map { workoutMapper.toWorkoutResponse(it) }.toList()

    fun getWorkoutDetails(userId: UUID, workoutId: UUID): WorkoutDetailsResponse {
        val workout = workoutRepository.findById(workoutId)
            .orElseThrow { ResourceNotFoundException("User does not belong to workout $workoutId") }
        if (workout.userId != userId) throw ResourceNotFoundException("User does not belong to workout $workoutId")
        val workoutExercises = workoutExerciseRepository.findByWorkout_Id(workoutId)
        return workoutMapper.toWorkoutDetailsResponse(workout, workoutExercises)
    }

}