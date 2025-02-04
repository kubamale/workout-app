package malewicz.jakub.workout_service.set.services

import malewicz.jakub.workout_service.exceptions.BadRequestException
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import malewicz.jakub.workout_service.set.dtos.SetDetailsDto
import malewicz.jakub.workout_service.set.dtos.SetType
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import malewicz.jakub.workout_service.set.repositories.SetRepository
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class SetServiceTest {

    private val workout = WorkoutEntity(UUID.randomUUID(), "legs", UUID.randomUUID())
    private val exercise = ExerciseEntity(
        UUID.randomUUID(),
        "exercise",
        MuscleGroup.LATS,
        "desc",
        ExerciseType.CARDIO,
        Equipment.EXERCISE_BALL
    )

    @Mock
    private lateinit var setRepository: SetRepository

    @Mock
    private lateinit var workoutExerciseRepository: WorkoutExerciseRepository

    @InjectMocks
    private lateinit var setService: SetService

    @Test
    fun `update sets should throw ResourceNotFoundException when no workout exercise was found`() {
        val workoutExerciseId = UUID.randomUUID()
        `when`(workoutExerciseRepository.findById(workoutExerciseId)).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { setService.updateSets(listOf(), workoutExerciseId) }
    }

    @Test
    fun `update sets should throw BadRequestException when incorrect amount of sets passed`() {
        val workoutExercise = WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0)
        `when`(workoutExerciseRepository.findById(workoutExercise.id!!)).thenReturn(Optional.of(workoutExercise))
        `when`(setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)).thenReturn(
            mutableListOf(
                WeightSetEntity(
                    UUID.randomUUID(),
                    0,
                    workoutExercise,
                    10,
                    10.0
                )
            )
        )
        assertThrows<BadRequestException> { setService.updateSets(mutableListOf(), workoutExercise.id!!) }
    }

    @Test
    fun `update sets should throw BadRequestException when weight not set in WeightSet`() {
        val workoutExercise = WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0)
        val set = WeightSetEntity(UUID.randomUUID(), 0, workoutExercise, 10, 10.0)
        `when`(workoutExerciseRepository.findById(workoutExercise.id!!)).thenReturn(Optional.of(workoutExercise))
        `when`(setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)).thenReturn(mutableListOf(set))
        val updateDetails = SetDetailsDto(set.id, 0, 10, null, null, null, SetType.WEIGHT)
        assertThrows<BadRequestException> { setService.updateSets(mutableListOf(updateDetails), workoutExercise.id!!) }
    }

    @Test
    fun `update sets should throw BadRequestException when reps not set in WeightSet`() {
        val workoutExercise = WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0)
        val set = WeightSetEntity(UUID.randomUUID(), 0, workoutExercise, 10, 10.0)
        `when`(workoutExerciseRepository.findById(workoutExercise.id!!)).thenReturn(Optional.of(workoutExercise))
        `when`(setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)).thenReturn(mutableListOf(set))
        val updateDetails = SetDetailsDto(set.id, 0, null, 10.0, null, null, SetType.WEIGHT)
        assertThrows<BadRequestException> { setService.updateSets(mutableListOf(updateDetails), workoutExercise.id!!) }
    }

    @Test
    fun `update weight sets should return unit when passed correct data`() {
        val workoutExercise = WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0)
        val existingSet = WeightSetEntity(UUID.randomUUID(), 0, workoutExercise, 10, 10.0)
        `when`(workoutExerciseRepository.findById(workoutExercise.id!!)).thenReturn(Optional.of(workoutExercise))
        `when`(setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)).thenReturn(mutableListOf(existingSet))
        val updateDetails = SetDetailsDto(existingSet.id, 0, 10, 10.0, null, null, SetType.WEIGHT)
        val newSetDetails = SetDetailsDto(null, 1, 10, 10.0, null, null, SetType.WEIGHT)
        setService.updateSets(mutableListOf(updateDetails, newSetDetails), workoutExercise.id!!)
    }

    @Test
    fun `update time sets should return unit when passed correct data`() {
        val workoutExercise = WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0)
        val existingSet = TimeSetEntity(UUID.randomUUID(), 0, workoutExercise, 10, 10.0)
        `when`(workoutExerciseRepository.findById(workoutExercise.id!!)).thenReturn(Optional.of(workoutExercise))
        `when`(setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)).thenReturn(mutableListOf(existingSet))
        val updateDetails = SetDetailsDto(existingSet.id, 0, 10, 10.0, 20, null, SetType.TIME)
        val newSetDetails = SetDetailsDto(null, 1, 10, 10.0, 10, null, SetType.TIME)
        setService.updateSets(mutableListOf(updateDetails, newSetDetails), workoutExercise.id!!)
    }

    @Test
    fun `update distance sets should return unit when passed correct data`() {
        val workoutExercise = WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0)
        val existingSet = DistanceSetEntity(UUID.randomUUID(), 0, workoutExercise, 10.0)
        `when`(workoutExerciseRepository.findById(workoutExercise.id!!)).thenReturn(Optional.of(workoutExercise))
        `when`(setRepository.findAllByWorkoutExerciseId(workoutExercise.id!!)).thenReturn(mutableListOf(existingSet))
        val updateDetails = SetDetailsDto(existingSet.id, 0, null, null, null, 10000.0, SetType.DISTANCE)
        val newSetDetails = SetDetailsDto(null, 1, null, null, null, 5000.0, SetType.DISTANCE)
        setService.updateSets(mutableListOf(updateDetails, newSetDetails), workoutExercise.id!!)
    }
}