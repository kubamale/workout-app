package malewicz.jakub.workout_service.exercise.services

import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.mappers.ExerciseMapper
import malewicz.jakub.workout_service.exercise.repositories.ExerciseRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExerciseService(
    private val exerciseMapper: ExerciseMapper,
    private val exerciseRepository: ExerciseRepository
) {
    fun getExerciseDetails(id: UUID) =
        exerciseMapper.toExerciseDetails(
            exerciseRepository.findById(id).orElseThrow({ ResourceNotFoundException("No exercise found for id $id") })
        )
}