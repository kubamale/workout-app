package malewicz.jakub.workout_service.exercise.services

import malewicz.jakub.workout_service.dtos.FilterRequest
import malewicz.jakub.workout_service.dtos.PageableResponse
import malewicz.jakub.workout_service.exceptions.BadRequestException
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseBasicsResponse
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseReorderRequest
import malewicz.jakub.workout_service.exercise.mappers.ExerciseMapper
import malewicz.jakub.workout_service.exercise.repositories.ExerciseRepository
import malewicz.jakub.workout_service.exercise.repositories.ExerciseSpecification
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExerciseService(
    private val exerciseMapper: ExerciseMapper,
    private val exerciseRepository: ExerciseRepository,
    private val workoutRepository: WorkoutRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) {
    fun getExerciseDetails(id: UUID) =
        exerciseMapper.toExerciseDetails(
            exerciseRepository.findById(id).orElseThrow({ ResourceNotFoundException("No exercise found for id $id") })
        )

    fun getAllExercises(pageable: Pageable, filters: List<FilterRequest>): PageableResponse<ExerciseBasicsResponse> {
        val specification = ExerciseSpecification(filters)
        val page = exerciseRepository.findAll(specification, pageable)
        return PageableResponse(
            page.totalElements,
            page.pageable.pageNumber,
            page.pageable.pageSize,
            page.hasNext(),
            page.totalPages,
            page.content.map { exerciseMapper.toBasicResponse(it) }.toList()
        )
    }

    fun addExerciseToWorkout(exerciseRequest: ExerciseCreateRequest<*>, userId: UUID): UUID {
        val workout = workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)
            .orElseThrow { ResourceNotFoundException("No workout with id ${exerciseRequest.workoutId} found for user id $userId") }
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
        newExercise.sets.forEach { it.workoutExercise = newExercise }
        return workoutRepository.save(workout).workoutExercises.find { it.exerciseOrder == exerciseOrder }?.id!!
    }

    fun reorderExercises(reorderRequest: ExerciseReorderRequest, userId: UUID) {
        val workout = workoutRepository.findByIdAndUserId(reorderRequest.workoutId, userId)
            .orElseThrow { ResourceNotFoundException("No workout with id ${reorderRequest.workoutId} found for user id $userId") }
        if (reorderRequest.exerciseOrder.size != workout.workoutExercises.size) {
            throw BadRequestException("Reorder map size is ${reorderRequest.exerciseOrder.size} but expected to find ${workout.workoutExercises.size}.")
        }
        workout.workoutExercises.sortedBy {
            reorderRequest.exerciseOrder[it.id] ?: throw BadRequestException("No exercise found for id $userId")
        }.forEachIndexed { index, exercise -> exercise.exerciseOrder = index }
        workoutRepository.save(workout)
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

    fun deleteFromWorkout(id: UUID, workoutId: UUID) {
        val workoutExercise = workoutExerciseRepository.findByIdAndWorkoutId(id, workoutId)
            .orElseThrow { ResourceNotFoundException("No exercise $id found in workout $workoutId.") }
        workoutExerciseRepository.delete(workoutExercise)
    }
}