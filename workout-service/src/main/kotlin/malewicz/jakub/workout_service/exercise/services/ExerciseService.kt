package malewicz.jakub.workout_service.exercise.services

import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.mappers.ExerciseMapper
import malewicz.jakub.workout_service.exercise.repositories.ExerciseRepository
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExerciseService(
    private val exerciseMapper: ExerciseMapper,
    private val exerciseRepository: ExerciseRepository,
    private val workoutRepository: WorkoutRepository,
) {
    fun getExerciseDetails(id: UUID) =
        exerciseMapper.toExerciseDetails(
            exerciseRepository.findById(id).orElseThrow({ ResourceNotFoundException("No exercise found for id $id") })
        )

    fun addExerciseToWorkout(exerciseRequest: ExerciseCreateRequest<*>, userId: UUID): UUID {
        val workout = workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)
            .orElseThrow { ResourceNotFoundException("No workout with id ${exerciseRequest.workoutId} found for user id ${exerciseRequest.workoutId}") }
        val exercise = exerciseRepository.findById(exerciseRequest.exerciseId)
            .orElseThrow { ResourceNotFoundException("No exercise found for id ${exerciseRequest.exerciseId}") }
        val sets: MutableList<SetEntity> = createSets(exerciseRequest)
        val exerciseOrder = getNewExercisesOrder(workout, exerciseRequest.order)
        val newExercise = WorkoutExerciseEntity(workout, exercise, sets, exerciseOrder)
        workout.workoutExercises.forEach {
            if (it.exerciseOrder >= newExercise.exerciseOrder) {
                it.exerciseOrder++
            }
        }
        workout.workoutExercises.add(newExercise)
        return workoutRepository.save(workout).workoutExercises.find { it.exerciseOrder == exerciseOrder }?.id!!
    }

    private fun createSets(exerciseRequest: ExerciseCreateRequest<*>): MutableList<SetEntity> {
        val timeSet = exerciseRequest.sets
            .filterIsInstance<TimeSetCreateRequest>()
            .sortedBy { it.setNumber }
            .mapIndexed { index, set -> TimeSetEntity(index, set.time, set.weight) }

        if (timeSet.isNotEmpty()) return timeSet.toMutableList()

        val weightSets = exerciseRequest.sets.filterIsInstance<WeightSetCreateRequest>()
            .sortedBy { it.setNumber }
            .mapIndexed { index, set -> WeightSetEntity(index, set.reps, set.weight) }

        if (weightSets.isNotEmpty()) return weightSets.toMutableList()

        val distanceSets = exerciseRequest.sets
            .filterIsInstance<DistanceSetCreateRequest>()
            .sortedBy { it.setNumber }
            .mapIndexed { index, set -> DistanceSetEntity(index, set.distance) }

        return distanceSets.toMutableList()
    }

    private fun getNewExercisesOrder(workout: WorkoutEntity, order: Int): Int {
        return if (order > workout.workoutExercises.size) workout.workoutExercises.size else order
    }
}