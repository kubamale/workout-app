package malewicz.jakub.statistics_service.statistics.services

import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import malewicz.jakub.statistics_service.statistics.dtos.OverallStatistics
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.util.*

@ExtendWith(MockitoExtension::class)
class StatisticsServiceTest {

  @Mock
  lateinit var workoutService: WorkoutService

  @Mock
  lateinit var measurementService: MeasurementService

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
}