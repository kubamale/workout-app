package malewicz.jakub.workout_service.exercise.services

import java.util.*
import malewicz.jakub.workout_service.exceptions.BadRequestException
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseReorderRequest
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.SetType
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import malewicz.jakub.workout_service.workout.entities.*
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.springframework.stereotype.Service

@Service
class ExerciseService(
  private val workoutRepository: WorkoutRepository,
  private val workoutExerciseRepository: WorkoutExerciseRepository
) {
  fun addExerciseToWorkout(exerciseRequest: ExerciseCreateRequest<*>, userId: UUID): UUID {
    val workout =
      workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId).orElseThrow {
        ResourceNotFoundException(
          "No workout with id ${exerciseRequest.workoutId} found for user id $userId"
        )
      }
    val exercise: WorkoutExerciseEntity = when (exerciseRequest.exerciseType) {
      SetType.WEIGHT -> createWeightExercise(exerciseRequest, workout)
      SetType.TIME -> createTimeExercise(exerciseRequest, workout)
      SetType.DISTANCE -> createDistanceExercise(exerciseRequest, workout)
    }
    workout.workoutExercises.forEachIndexed { index, workoutExercise -> if (index >= exercise.exerciseOrder) workoutExercise.exerciseOrder += 1 }
    workout.workoutExercises.add(exercise)

    return workoutRepository.save(workout).workoutExercises.find { it.exerciseOrder == exercise.exerciseOrder }?.id!!
  }

  fun reorderExercises(reorderRequest: ExerciseReorderRequest, userId: UUID) {
    val workout =
      workoutRepository.findByIdAndUserId(reorderRequest.workoutId, userId).orElseThrow {
        ResourceNotFoundException(
          "No workout with id ${reorderRequest.workoutId} found for user id $userId"
        )
      }
    if (reorderRequest.exerciseOrder.size != workout.workoutExercises.size) {
      throw BadRequestException(
        "Reorder map size is ${reorderRequest.exerciseOrder.size} but expected to find ${workout.workoutExercises.size}."
      )
    }
    workout.workoutExercises
      .sortedBy {
        reorderRequest.exerciseOrder[it.id]
          ?: throw BadRequestException("No exercise found for id $userId")
      }
      .forEachIndexed { index, exercise -> exercise.exerciseOrder = index }
    workoutRepository.save(workout)
  }

  private fun createWeightExercise(
    exerciseRequest: ExerciseCreateRequest<*>,
    workout: WorkoutEntity
  ): WeightWorkoutExerciseEntity {
    val sets = exerciseRequest.sets.filterIsInstance<WeightSetCreateRequest>()
      .map { WeightSetEntity(it.setNumber, it.reps, it.weight) }
      .toMutableList()

    if (sets.size != exerciseRequest.sets.size) {
      throw BadRequestException("Provided sets are not weight sets.")
    }

    sets.forEachIndexed { index, set -> set.setOrder = index }

    return WeightWorkoutExerciseEntity(
      workout,
      exerciseRequest.exerciseId,
      getNewExercisesOrder(workout, exerciseRequest.order),
      sets
    ).apply {
      sets.forEach { it.workoutExercise = this }
    }
  }

  private fun createTimeExercise(
    exerciseRequest: ExerciseCreateRequest<*>,
    workout: WorkoutEntity
  ): TimeWorkoutExerciseEntity {
    val sets = exerciseRequest.sets.filterIsInstance<TimeSetCreateRequest>()
      .map { TimeSetEntity(it.setNumber, it.time, it.weight) }
      .toMutableList()

    if (sets.size != exerciseRequest.sets.size) {
      throw BadRequestException("Provided sets are not weight sets.")
    }

    sets.forEachIndexed { index, set -> set.setOrder = index }

    return TimeWorkoutExerciseEntity(
      workout,
      exerciseRequest.exerciseId,
      getNewExercisesOrder(workout, exerciseRequest.order),
      sets
    ).apply {
      sets.forEach { it.workoutExercise = this }
    }
  }


  private fun createDistanceExercise(
    exerciseRequest: ExerciseCreateRequest<*>,
    workout: WorkoutEntity
  ): DistanceWorkoutExerciseEntity {
    val sets = exerciseRequest.sets.filterIsInstance<DistanceSetCreateRequest>()
      .map { DistanceSetEntity(it.setNumber, it.distance) }
      .toMutableList()

    if (sets.size != exerciseRequest.sets.size) {
      throw BadRequestException("Provided sets are not weight sets.")
    }

    sets.forEachIndexed { index, set -> set.setOrder = index }

    return DistanceWorkoutExerciseEntity(
      workout,
      exerciseRequest.exerciseId,
      getNewExercisesOrder(workout, exerciseRequest.order),
      sets
    ).apply {
      sets.forEach { it.workoutExercise = this }
    }
  }

  private fun getNewExercisesOrder(workout: WorkoutEntity, order: Int): Int {
    return if (order > workout.workoutExercises.size) workout.workoutExercises.size else order
  }

  fun deleteFromWorkout(id: UUID, workoutId: UUID) {
    val workoutExercise =
      workoutExerciseRepository.findByIdAndWorkoutId(id, workoutId).orElseThrow {
        ResourceNotFoundException("No exercise $id found in workout $workoutId.")
      }
    workoutExerciseRepository.delete(workoutExercise)
  }
}
