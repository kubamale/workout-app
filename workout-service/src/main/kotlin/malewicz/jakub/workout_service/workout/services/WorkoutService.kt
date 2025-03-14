package malewicz.jakub.workout_service.workout.services

import malewicz.jakub.workout_service.clients.ExerciseClient
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.set.repositories.SetRepository
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.mappers.WorkoutMapper
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class WorkoutService(
  private val workoutRepository: WorkoutRepository,
  private val workoutMapper: WorkoutMapper,
  private val setRepository: SetRepository,
  private val exerciseClient: ExerciseClient,
) {

  fun createWorkout(workoutCreateRequest: WorkoutCreateRequest, userId: UUID): UUID {
    val workout = workoutRepository.save(WorkoutEntity(null, workoutCreateRequest.name, userId))
    return workout.id!!
  }

  fun getUserWorkouts(userId: UUID) =
    workoutRepository.findByUserId(userId).map { workoutMapper.toWorkoutResponse(it) }.toList()

  fun getWorkoutDetails(userId: UUID, workoutId: UUID): WorkoutDetailsResponse {
    val workout =
      workoutRepository.findByIdAndUserId(workoutId, userId).orElseThrow {
        ResourceNotFoundException("User does not belong to workout $workoutId")
      }
    val workoutExercises = workout.workoutExercises
    val sets = setRepository.findAllByWorkoutExerciseIn(workoutExercises)
    val exercises = exerciseClient.getExercisesDetails(workoutExercises.map { it.exerciseId }).associateBy { it.id }
    val workoutExerciseDetails =
      workoutExercises
        .map {
          workoutMapper.toWorkoutExerciseDetails(
            it,
            exercises[it.exerciseId],
            sets.filter { set -> set.workoutExercise?.id == it.id })
        }
        .toMutableList()
    return workoutMapper.toWorkoutDetailsResponse(workout, workoutExerciseDetails)
  }

  fun getByIds(ids: List<UUID>) =
    workoutRepository.findAllByIdIn(ids).map { workoutMapper.toWorkoutResponse(it) }.toList()
}
