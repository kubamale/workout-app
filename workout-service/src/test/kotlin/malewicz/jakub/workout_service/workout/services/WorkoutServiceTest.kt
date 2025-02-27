package malewicz.jakub.workout_service.workout.services

import malewicz.jakub.workout_service.clients.ExerciseClient
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.exercise.models.Equipment
import malewicz.jakub.workout_service.exercise.models.ExerciseType
import malewicz.jakub.workout_service.exercise.models.MuscleGroup
import malewicz.jakub.workout_service.set.dtos.SetDetailsDto
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.dtos.WorkoutExerciseDetails
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.mappers.WorkoutMapper
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class WorkoutServiceTest {

  @Mock
  private lateinit var workoutRepository: WorkoutRepository

  @Mock
  private lateinit var workoutMapper: WorkoutMapper

  @Mock
  private lateinit var workoutExerciseRepository: WorkoutExerciseRepository

  @Mock
  private lateinit var exerciseClient: ExerciseClient


  @InjectMocks
  private lateinit var workoutService: WorkoutService


  @Test
  fun `create workout returns workout id when passed correct data`() {
    val workout = WorkoutCreateRequest("push")
    val userId = UUID.randomUUID()
    val savedWorkout = WorkoutEntity(UUID.randomUUID(), "push", userId)
    `when`(workoutRepository.save(any())).thenReturn(savedWorkout)

    val result = workoutService.createWorkout(workout, userId)
    assertThat(result).isNotNull
    assertThat(result).isEqualTo(savedWorkout.id)
  }

  @Test
  fun `get user workouts returns workouts`() {
    val userId = UUID.randomUUID()
    `when`(workoutRepository.findByUserId(userId)).thenReturn(
      listOf(
        WorkoutEntity(UUID.randomUUID(), "push", userId),
        WorkoutEntity(UUID.randomUUID(), "pull", userId)
      )
    )
    `when`(workoutMapper.toWorkoutResponse(this.any(WorkoutEntity::class.java))).thenReturn(
      WorkoutResponse(
        UUID.randomUUID(),
        "Legs"
      ), WorkoutResponse(UUID.randomUUID(), "Legs")
    )
    val result = workoutService.getUserWorkouts(userId)
    assertThat(result).isNotNull
    assertThat(result).hasSize(2)
  }

  @Test
  fun `get workout details throws ResourceNotFoundException when no workouts found`() {
    val workoutId = UUID.randomUUID()
    val userId = UUID.randomUUID()
    `when`(workoutRepository.findByIdAndUserId(workoutId, userId)).thenReturn(Optional.empty())
    assertThrows<ResourceNotFoundException> { workoutService.getWorkoutDetails(userId, workoutId) }
  }


  @Test
  fun `get workout details returns workout details`() {
    val workoutId = UUID.randomUUID()
    val userId = UUID.randomUUID()
    val workout = WorkoutEntity(workoutId, "push", userId)
    val set = WeightSetEntity(UUID.randomUUID(), 0, null, 10, 20.0)
    val workoutExercise = WorkoutExerciseEntity(
      UUID.randomUUID(),
      workout,
      UUID.randomUUID(),
      mutableListOf(set),
      0
    )
    val exerciseDetails = ExerciseDetails(
      workoutExercise.exerciseId,
      "legs",
      MuscleGroup.LATS,
      "Desc",
      ExerciseType.CARDIO,
      Equipment.NONE
    )

    `when`(workoutRepository.findByIdAndUserId(workoutId, userId)).thenReturn(Optional.of(workout))
    `when`(workoutExerciseRepository.findByWorkoutId(workoutId)).thenReturn(listOf(workoutExercise))
    `when`(exerciseClient.getExercisesDetails(listOf(workoutExercise.exerciseId))).thenReturn(
      listOf(
        exerciseDetails
      )
    )
    val workoutExerciseDetails = WorkoutExerciseDetails(
      workoutExercise.id!!, exerciseDetails, workoutExercise.exerciseOrder, mutableListOf(
        SetDetailsDto(set.id!!, set.setOrder, set.reps, set.weight)
      )
    )
    `when`(workoutMapper.toWorkoutExerciseDetails(workoutExercise, exerciseDetails)).thenReturn(workoutExerciseDetails)
    `when`(workoutMapper.toWorkoutDetailsResponse(workout, mutableListOf(workoutExerciseDetails))).thenReturn(
      WorkoutDetailsResponse(
        workoutId,
        workout.name,
        mutableListOf(
          workoutExerciseDetails
        )
      )
    )
    val result = workoutService.getWorkoutDetails(userId, workoutId)
    assertThat(result).isNotNull
    assertThat(result.id).isEqualTo(workoutId)
    assertThat(result.name).isEqualTo(workout.name)
    assertThat(result.exercises).hasSize(1)
    assertThat(result.exercises[0].sets).hasSize(1)
    assertThat(result.exercises[0].sets[0].id).isEqualTo(set.id)

  }

  private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}