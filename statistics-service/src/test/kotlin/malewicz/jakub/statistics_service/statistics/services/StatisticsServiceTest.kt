package malewicz.jakub.statistics_service.statistics.services

import malewicz.jakub.statistics_service.exercises.entities.DistanceExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.TimeExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.WeightExerciseEntity
import malewicz.jakub.statistics_service.exercises.services.ExerciseService
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import malewicz.jakub.statistics_service.sets.dtos.SetType
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import malewicz.jakub.statistics_service.statistics.dtos.OverallStatistics
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class StatisticsServiceTest {

  @Mock
  lateinit var workoutService: WorkoutService

  @Mock
  lateinit var measurementService: MeasurementService

  @Mock
  lateinit var exerciseService: ExerciseService

  @InjectMocks
  lateinit var statisticsService: StatisticsService

  private val measurementDetails = MeasurementDetails(
    weight = 100.0,
    bodyFat = 20.0,
    leftArm = 50.0,
    rightArm = 50.0,
    chest = 100.0,
    waist = 100.0,
    hips = 100.0,
    leftThigh = 100.0,
    rightThigh = 100.0,
    leftCalf = 60.0,
    rightCalf = 60.0,
    shoulders = 100.0,
  )

  @Test
  fun `get overall statistics should return overall statistics`() {
    val userId = UUID.randomUUID()
    val workoutInfo = WorkoutBasicInfo(UUID.randomUUID(), "legs")
    val overallStatistics = OverallStatistics(1, listOf(workoutInfo), listOf(measurementDetails))
    `when`(workoutService.getDaysSinceLastWorkout(userId)).thenReturn(overallStatistics.daysSinceWorkout)
    `when`(workoutService.getWorkoutsFromCurrentWeek(userId)).thenReturn(overallStatistics.thisWeeksWorkouts)
    `when`(measurementService.getMeasurementsSince(any(), any())).thenReturn(overallStatistics.thirtyDaysMeasurements)

    val result = statisticsService.getOverallStatistics(userId)
    kotlin.test.assertEquals(overallStatistics, result)
  }

  @Test
  fun `get exercise statistics should return exercise statistics`() {
    val userId = UUID.randomUUID()
    val workout = UserWorkoutEntity(UUID.randomUUID(), userId, UUID.randomUUID(), LocalDateTime.now())
    val exerciseId = UUID.randomUUID()
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

    val exercises = listOf(weightExercise, distanceExercise, timeExercise)
    `when`(exerciseService.getExercisesForUser(userId, exerciseId)).thenReturn(exercises)
    `when`(exerciseService.getStatisticsForExercises(exercises)).thenReturn(
      mapOf(
        Pair(SetType.WEIGHT, listOf(weightStatistic)),
        Pair(SetType.TIME, listOf(timeStatistics)),
        Pair(SetType.DISTANCE, listOf(distanceStatistics))
      )
    )

    val result = statisticsService.getExerciseStatistics(exerciseId, userId)
    assertThat(result[SetType.WEIGHT]).hasSize(1)
    assertThat(result[SetType.TIME]).hasSize(1)
    assertThat(result[SetType.DISTANCE]).hasSize(1)

  }
}