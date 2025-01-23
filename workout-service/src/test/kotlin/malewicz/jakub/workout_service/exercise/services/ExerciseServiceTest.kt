package malewicz.jakub.workout_service.exercise.services

import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import malewicz.jakub.workout_service.exercise.mappers.ExerciseMapper
import malewicz.jakub.workout_service.exercise.repositories.ExerciseRepository
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class ExerciseServiceTest {

    @Mock
    private lateinit var exerciseRepository: ExerciseRepository

    @Mock
    private lateinit var exerciseMapper: ExerciseMapper

    @Mock
    private lateinit var workoutRepository: WorkoutRepository

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
        assertThrows<ResourceNotFoundException> { exerciseService.getExerciseDetails(id) }
    }

    @Test
    fun `addExerciseToWorkout should throw ResourceNotFoundException when workout with provided id and user id does not exist`() {
        val userId = UUID.randomUUID()
        val exerciseRequest =
            ExerciseCreateRequest<WeightSetCreateRequest>(UUID.randomUUID(), UUID.randomUUID(), 0, mutableListOf())
        `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { exerciseService.addExerciseToWorkout(exerciseRequest, userId) }
    }

    @Test
    fun `addExerciseToWorkout should throw ResourceNotFoundException when exercise with provided id does not exist`() {
        val userId = UUID.randomUUID()
        val exerciseRequest =
            ExerciseCreateRequest<WeightSetCreateRequest>(UUID.randomUUID(), UUID.randomUUID(), 0, mutableListOf())
        `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)).thenReturn(
            Optional.of(
                WorkoutEntity(
                    UUID.randomUUID(),
                    "Push",
                    userId,
                    mutableListOf()
                )
            )
        )
        `when`(exerciseRepository.findById(exerciseRequest.exerciseId)).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { exerciseService.addExerciseToWorkout(exerciseRequest, userId) }
    }

    @Test
    fun `addExerciseToWorkout should return exercise_workout id when passed Weight set data`() {
        val userId = UUID.randomUUID()
        val exerciseRequest =
            ExerciseCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), 0,
                mutableListOf(
                    WeightSetCreateRequest(null, 0, 10, 23.5)
                ),
            )
        val workout = WorkoutEntity(
            UUID.randomUUID(),
            "Push",
            userId,
            mutableListOf()
        )
        val exercise = ExerciseEntity(
            UUID.randomUUID(),
            "Push",
            MuscleGroup.ABDOMINALS,
            "description",
            ExerciseType.STRENGTH,
            Equipment.EXERCISE_BALL,
        )

        `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)).thenReturn(Optional.of(workout))
        `when`(exerciseRepository.findById(exerciseRequest.exerciseId)).thenReturn(Optional.of(exercise))
        val exerciseId = UUID.randomUUID()
        `when`(workoutRepository.save(workout)).thenReturn(
            WorkoutEntity(
                UUID.randomUUID(),
                "Push",
                userId,
                mutableListOf(
                    WorkoutExerciseEntity(exerciseId, workout, exercise, mutableListOf(), 0)
                ),
            ),
        )
        assertThat(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).isEqualTo(exerciseId)
        verify(workoutRepository).save(workout)
    }

    @Test
    fun `addExerciseToWorkout should return exercise_workout id when passed Time set data`() {
        val userId = UUID.randomUUID()
        val exerciseRequest =
            ExerciseCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), 1,
                mutableListOf(
                    TimeSetCreateRequest(null, 0, 10, 0.0)
                ),
            )
        val workout = WorkoutEntity(
            UUID.randomUUID(),
            "Push",
            userId,
            mutableListOf()
        )

        val exercise = ExerciseEntity(
            UUID.randomUUID(),
            "Push",
            MuscleGroup.ABDOMINALS,
            "description",
            ExerciseType.STRENGTH,
            Equipment.EXERCISE_BALL,
        )

        workout.workoutExercises = mutableListOf(
            WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0),
            WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 1),
            WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 2),

        )

        `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)).thenReturn(Optional.of(workout))
        `when`(exerciseRepository.findById(exerciseRequest.exerciseId)).thenReturn(Optional.of(exercise))
        val exerciseId = UUID.randomUUID()
        `when`(workoutRepository.save(workout)).thenReturn(
            WorkoutEntity(
                UUID.randomUUID(),
                "Push",
                userId,
                mutableListOf(
                    WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 0),
                    WorkoutExerciseEntity(exerciseId, workout, exercise, mutableListOf(), 1),
                    WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 2),
                    WorkoutExerciseEntity(UUID.randomUUID(), workout, exercise, mutableListOf(), 3),
                ),
            ),
        )
        assertThat(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).isEqualTo(exerciseId)
        verify(workoutRepository).save(workout)
    }

    @Test
    fun `addExerciseToWorkout should return exercise_workout id when passed distance set data`() {
        val userId = UUID.randomUUID()
        val exerciseRequest =
            ExerciseCreateRequest(
                UUID.randomUUID(), UUID.randomUUID(), 2,
                mutableListOf(
                    DistanceSetCreateRequest(null, 0, 10.0)
                ),
            )
        val workout = WorkoutEntity(
            UUID.randomUUID(),
            "Push",
            userId,
            mutableListOf()
        )
        val exercise = ExerciseEntity(
            UUID.randomUUID(),
            "Push",
            MuscleGroup.ABDOMINALS,
            "description",
            ExerciseType.STRENGTH,
            Equipment.EXERCISE_BALL,
        )

        `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId)).thenReturn(Optional.of(workout))
        `when`(exerciseRepository.findById(exerciseRequest.exerciseId)).thenReturn(Optional.of(exercise))
        val exerciseId = UUID.randomUUID()
        `when`(workoutRepository.save(workout)).thenReturn(
            WorkoutEntity(
                UUID.randomUUID(),
                "Push",
                userId,
                mutableListOf(
                    WorkoutExerciseEntity(exerciseId, workout, exercise, mutableListOf(), 0)
                ),
            ),
        )
        assertThat(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).isEqualTo(exerciseId)
        verify(workoutRepository).save(workout)
    }
}