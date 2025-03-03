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
import malewicz.jakub.workout_service.workout.entities.*
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ExerciseServiceTest {

  @Mock
  private lateinit var workoutRepository: WorkoutRepository

  @Mock
  private lateinit var workoutExerciseRepository: WorkoutExerciseRepository

  @InjectMocks
  private lateinit var exerciseService: ExerciseService

  @Test
  fun `addExerciseToWorkout should throw ResourceNotFoundException when workout with provided id and user id does not exist`() {
    val userId = UUID.randomUUID()
    val exerciseRequest =
      ExerciseCreateRequest<WeightSetCreateRequest>(
        UUID.randomUUID(), UUID.randomUUID(), SetType.WEIGHT, 0, mutableListOf()
      )
    `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId))
      .thenReturn(Optional.empty())
    assertThrows<ResourceNotFoundException> {
      exerciseService.addExerciseToWorkout(exerciseRequest, userId)
    }
  }

  @Test
  fun `addExerciseToWorkout should return exercise_workout id when passed Weight set data`() {
    val userId = UUID.randomUUID()
    val exerciseRequest =
      ExerciseCreateRequest(
        UUID.randomUUID(),
        UUID.randomUUID(),
        SetType.WEIGHT,
        0,
        mutableListOf(WeightSetCreateRequest(null, 0, 10, 23.5)),
      )
    val workout = WorkoutEntity(UUID.randomUUID(), "Push", userId, mutableListOf())
    `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId))
      .thenReturn(Optional.of(workout))
    val workoutExerciseId = UUID.randomUUID()
    `when`(workoutRepository.save(workout))
      .thenReturn(
        WorkoutEntity(
          UUID.randomUUID(),
          "Push",
          userId,
          mutableListOf(
            WeightWorkoutExerciseEntity(workoutExerciseId, workout, UUID.randomUUID(), 0, mutableListOf())
          ),
        ),
      )
    assertThat(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).isEqualTo(workoutExerciseId)
    verify(workoutRepository).save(workout)
  }

  @Test
  fun `addExerciseToWorkout should return exercise_workout id when passed Time set data`() {
    val userId = UUID.randomUUID()
    val exerciseRequest =
      ExerciseCreateRequest(
        UUID.randomUUID(),
        UUID.randomUUID(),
        SetType.TIME,
        1,
        mutableListOf(TimeSetCreateRequest(null, 0, 10, 0.0)),
      )
    val workout = WorkoutEntity(UUID.randomUUID(), "Push", userId, mutableListOf())
    val exerciseId = UUID.randomUUID()

    workout.workoutExercises =
      mutableListOf(
        WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 0, mutableListOf()),
        WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 1, mutableListOf()),
        WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 2, mutableListOf()),
      )

    val workoutExerciseId = UUID.randomUUID()

    `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId))
      .thenReturn(Optional.of(workout))

    `when`(workoutRepository.save(workout))
      .thenReturn(
        WorkoutEntity(
          UUID.randomUUID(),
          "Push",
          userId,
          mutableListOf(
            TimeWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 0, mutableListOf()),
            TimeWorkoutExerciseEntity(workoutExerciseId, workout, exerciseId, 1, mutableListOf()),
            TimeWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 2, mutableListOf()),
            TimeWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 3, mutableListOf()),
          ),
        ),
      )
    assertThat(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).isEqualTo(workoutExerciseId)
    verify(workoutRepository).save(workout)
  }

  @Test
  fun `addExerciseToWorkout should return exercise_workout id when passed distance set data`() {
    val userId = UUID.randomUUID()
    val exerciseRequest =
      ExerciseCreateRequest(
        UUID.randomUUID(),
        UUID.randomUUID(),
        SetType.DISTANCE,
        2,
        mutableListOf(DistanceSetCreateRequest(null, 0, 10.0)),
      )
    val workout = WorkoutEntity(UUID.randomUUID(), "Push", userId, mutableListOf())
    val workoutExerciseId = UUID.randomUUID()
    `when`(workoutRepository.findByIdAndUserId(exerciseRequest.workoutId, userId))
      .thenReturn(Optional.of(workout))
    `when`(workoutRepository.save(workout))
      .thenReturn(
        WorkoutEntity(
          UUID.randomUUID(),
          "Push",
          userId,
          mutableListOf(
            DistanceWorkoutExerciseEntity(workoutExerciseId, workout, UUID.randomUUID(), 0, mutableListOf())
          ),
        ),
      )
    assertThat(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).isEqualTo(workoutExerciseId)
    verify(workoutRepository).save(workout)
  }

  @Test
  fun `reorder exercises should throw ResourceNotFoundException when Workout was not found`() {
    val userId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    val request = ExerciseReorderRequest(workoutId, hashMapOf())
    `when`(workoutRepository.findByIdAndUserId(workoutId, userId)).thenReturn(Optional.empty())
    assertThrows<ResourceNotFoundException> { exerciseService.reorderExercises(request, userId) }
  }

  @Test
  fun `reorder exercises should throw BadRequestException when passed incorrect map size`() {
    val userId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    val exerciseId1 = UUID.randomUUID()
    val exerciseId2 = UUID.randomUUID()
    val request = ExerciseReorderRequest(workoutId, hashMapOf(Pair(exerciseId1, 1)))
    val workout = WorkoutEntity(workoutId, "legs", userId, mutableListOf())
    val ex1 = WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId1, 0,  mutableListOf())
    val ex2 = WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId2, 1, mutableListOf())
    workout.workoutExercises = mutableListOf(ex1, ex2)

    `when`(workoutRepository.findByIdAndUserId(workoutId, userId)).thenReturn(Optional.of(workout))
    assertThrows<BadRequestException> { exerciseService.reorderExercises(request, userId) }
  }

  @Test
  fun `reorder exercises should throw BadRequestException when passed incorrect id in map`() {
    val userId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    val exerciseId1 = UUID.randomUUID()
    val exerciseId2 = UUID.randomUUID()
    val request =
      ExerciseReorderRequest(workoutId, hashMapOf(Pair(exerciseId1, 1), Pair(exerciseId2, 0)))
    val workout = WorkoutEntity(workoutId, "legs", userId, mutableListOf())
    val ex1 = WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId1, 0,  mutableListOf())
    val ex2 = WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId2, 1, mutableListOf())
    workout.workoutExercises = mutableListOf(ex1, ex2)

    `when`(workoutRepository.findByIdAndUserId(workoutId, userId)).thenReturn(Optional.of(workout))
    assertThrows<BadRequestException> { exerciseService.reorderExercises(request, userId) }
  }

  @Test
  fun `reorder exercises should save reordered exercises`() {
    val userId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    val exerciseId1 = UUID.randomUUID()
    val exerciseId2 = UUID.randomUUID()
    val request = ExerciseReorderRequest(workoutId, hashMapOf(Pair(exerciseId1, 1), Pair(exerciseId2, 0)))
    val workout = WorkoutEntity(workoutId, "legs", userId, mutableListOf())
    val ex1 = WeightWorkoutExerciseEntity(exerciseId1, workout, UUID.randomUUID(), 0,  mutableListOf())
    val ex2 = WeightWorkoutExerciseEntity(exerciseId2, workout, UUID.randomUUID(), 1, mutableListOf())
    workout.workoutExercises = mutableListOf(ex1, ex2)
    `when`(workoutRepository.findByIdAndUserId(workoutId, userId)).thenReturn(Optional.of(workout))
    exerciseService.reorderExercises(request, userId)
    verify(workoutRepository).save(workout)
  }

  @Test
  fun `delete from workout should throw ResourceNotFoundException when exercise in workout was not found`() {
    val exerciseId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    `when`(workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId))
      .thenReturn(Optional.empty())

    assertThrows<ResourceNotFoundException> {
      exerciseService.deleteFromWorkout(exerciseId, workoutId)
    }
  }

  @Test
  fun `delete from workout should delete exercise`() {
    val exerciseId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    val workout = WorkoutEntity(workoutId, "legs", UUID.randomUUID())
    val workoutExercise = WeightWorkoutExerciseEntity(UUID.randomUUID(), workout, exerciseId, 0, mutableListOf())
    `when`(workoutExerciseRepository.findByIdAndWorkoutId(exerciseId, workoutId))
      .thenReturn(Optional.of(workoutExercise))
    exerciseService.deleteFromWorkout(exerciseId, workoutId)
    verify(workoutExerciseRepository).delete(workoutExercise)
  }
}
