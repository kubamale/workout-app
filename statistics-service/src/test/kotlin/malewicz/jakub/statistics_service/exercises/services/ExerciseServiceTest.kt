package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.DistanceExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.TimeExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.WeightExerciseEntity
import malewicz.jakub.statistics_service.exercises.repositories.ExerciseRepository
import malewicz.jakub.statistics_service.sets.dtos.SetType
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class ExerciseServiceTest {

  @Mock
  private lateinit var exerciseRepository: ExerciseRepository

  @Mock
  private lateinit var weightExerciseService: WeightExerciseService

  @Mock
  private lateinit var timeExerciseService: TimeExerciseService

  @Mock
  private lateinit var distanceExerciseService: DistanceExerciseService

  @InjectMocks
  private lateinit var exerciseService: ExerciseService

  @Test
  fun `get statistics for exercises should return statistics by type`() {
    val workout = UserWorkoutEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now())
    val exerciseId = UUID.randomUUID()
    val weightStatistic = ExerciseStatistics(
      exerciseId = exerciseId,
      date = LocalDateTime.now(),
      maxWeight = 100.0,
      reps = 12,
      volume = 1200.0,
      oneRepMax = 143.0
    )
    val timeStatistics = ExerciseStatistics(
      exerciseId = exerciseId,
      date = LocalDateTime.now(),
      maxWeight = 100.0,
      time = 2000
    )
    val distanceStatistics = ExerciseStatistics(
      exerciseId = exerciseId,
      date = LocalDateTime.now(),
      distance = 23.4
    )

    val weightExercise = WeightExerciseEntity(
      UUID.randomUUID(),
      exerciseId,
      UUID.randomUUID(),
      workout
    )
    val distanceExercise = DistanceExerciseEntity(
      UUID.randomUUID(),
      exerciseId,
      UUID.randomUUID(),
      workout
    )
    val timeExercise = TimeExerciseEntity(
      UUID.randomUUID(),
      exerciseId,
      UUID.randomUUID(),
      workout
    )

    val exercises = listOf(weightExercise, distanceExercise, timeExercise)
    val weightExercises = listOf(weightExercise)
    `when`(weightExerciseService.createWeightStatistics(weightExercises)).thenReturn(listOf(weightStatistic))
    val timeExercises = listOf(timeExercise)
    `when`(timeExerciseService.createTimeStatistics(timeExercises)).thenReturn(listOf(timeStatistics))
    val distanceExercises = listOf(distanceExercise)
    `when`(distanceExerciseService.createDistanceStatistics(distanceExercises)).thenReturn(listOf(distanceStatistics))

    val result = exerciseService.getStatisticsForExercises(exercises)
    assertThat(result).hasSize(3)
    assertThat(result[SetType.TIME]).hasSize(1)
    assertThat(result[SetType.DISTANCE]).hasSize(1)
    assertThat(result[SetType.WEIGHT]).hasSize(1)
  }

  @Test
  fun `get exercises for user should return users exercises`() {
    val userId = UUID.randomUUID()
    val exerciseId = UUID.randomUUID()
    val workout = UserWorkoutEntity(UUID.randomUUID(), userId, UUID.randomUUID(), LocalDateTime.now())
    val exercises = listOf(
      DistanceExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      ),
      DistanceExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      )
    )
    `when`(exerciseRepository.findAllByExerciseIdAndWorkoutUserId(exerciseId, userId)).thenReturn(exercises)
    val result = exerciseService.getExercisesForUser(userId, exerciseId)
    assertThat(result).isEqualTo(exercises)
  }
}