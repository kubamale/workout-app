package malewicz.jakub.workout_service.set.services

import malewicz.jakub.workout_service.exceptions.BadRequestException
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.set.dtos.SetDetailsDto
import malewicz.jakub.workout_service.set.dtos.SetType
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import malewicz.jakub.workout_service.set.repositories.SetRepository
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class SetService(
    private val setRepository: SetRepository,
    private val workoutExerciseRepository: WorkoutExerciseRepository
) {
    fun updateSets(setsDetails: List<SetDetailsDto>, workoutExerciseId: UUID) {
        val workoutExercise = workoutExerciseRepository.findById(workoutExerciseId).orElseThrow {
            ResourceNotFoundException("No workout exercise with id $workoutExerciseId.")
        }
        val passedExistingSets = setsDetails.count { it.id != null }
        val existingSets = setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)
        if (passedExistingSets != existingSets.size) {
            throw BadRequestException("Expected ${existingSets.size} sets with id passed but found $passedExistingSets.")
        }

        existingSets.forEach { set ->
            val updatedSet =
                setsDetails.firstOrNull { it.id == set.id } ?: throw BadRequestException("Incorrect set id passed.")
            updateSet(set, updatedSet)
        }

        val newSets: List<SetEntity> = setsDetails.filter { it.id == null }.map {
            createNewSet(workoutExercise, it)
        }

        val allSets = (existingSets + newSets).sortedBy { it.setOrder }.mapIndexed { index, set ->
            set.apply { setOrder = index }
        }

        setRepository.saveAll(allSets)
    }

    private fun updateSet(set: SetEntity, updateDetails: SetDetailsDto) {
        when (set) {
            is DistanceSetEntity -> set.apply {
                distance = updateDetails.distance ?: throw BadRequestException("Distance not set.")
                setOrder = updateDetails.order
            }

            is WeightSetEntity -> set.apply {
                weight = updateDetails.weight ?: throw BadRequestException("Weight not set.")
                reps = updateDetails.reps ?: throw BadRequestException("Reps not set.")
                setOrder = updateDetails.order
            }

            is TimeSetEntity -> set.apply {
                weight = updateDetails.weight ?: throw BadRequestException("Weight not set.")
                time = updateDetails.time ?: throw BadRequestException("Time not set.")
                setOrder = updateDetails.order
            }
        }
    }

    private fun createNewSet(workoutExercise: WorkoutExerciseEntity, details: SetDetailsDto): SetEntity {
        return when (details.type) {
            SetType.WEIGHT -> WeightSetEntity(
                id = null,
                setOrder = details.order,
                workoutExercise = workoutExercise,
                reps = details.reps ?: throw BadRequestException("Reps not set."),
                weight = details.weight ?: throw BadRequestException("Weight not set.")
            )

            SetType.TIME -> TimeSetEntity(
                id = null,
                setOrder = details.order,
                workoutExercise = workoutExercise,
                time = details.time ?: throw BadRequestException("Time not set."),
                weight = details.weight ?: throw BadRequestException("Weight not set.")
            )

            SetType.DISTANCE -> DistanceSetEntity(
                id = null,
                setOrder = details.order,
                workoutExercise = workoutExercise,
                distance = details.distance ?: throw BadRequestException("Distance not set.")
            )
        }
    }
}