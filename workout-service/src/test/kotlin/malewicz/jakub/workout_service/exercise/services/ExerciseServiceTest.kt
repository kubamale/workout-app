package malewicz.jakub.workout_service.exercise.services

import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import malewicz.jakub.workout_service.exercise.mappers.ExerciseMapper
import malewicz.jakub.workout_service.exercise.repositories.ExerciseRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ExerciseServiceTest {

    @Mock
    private lateinit var exerciseRepository: ExerciseRepository
    @Mock
    private lateinit var exerciseMapper: ExerciseMapper
    @InjectMocks
    private lateinit var exerciseService: ExerciseService

    @Test
    fun `getExerciseDetails should return exercise details when passed correct id`() {
        val id = UUID.randomUUID()
        val exercise =
            ExerciseEntity(id, "Crunch", MuscleGroup.ABDOMINALS, "description", ExerciseType.STRENGTH, Equipment.NONE)
        `when`(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise))
        val exerciseDetails = ExerciseDetails(
            id,
            exercise.name,
            exercise.muscleGroup,
            exercise.description,
            exercise.type,
            exercise.equipment,
            exercise.pictureURL
        )
        `when`(exerciseMapper.toExerciseDetails(exercise)).thenReturn(exerciseDetails)
        val result = exerciseService.getExerciseDetails(id)
        assertEquals(exerciseDetails, result)
    }

    @Test
    fun `getExerciseDetails should throw ResourceNotFoundException when exercise with provided id is not found`() {
        val id = UUID.randomUUID()
        `when`(exerciseRepository.findById(id)).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> {exerciseService.getExerciseDetails(id)}
    }
}