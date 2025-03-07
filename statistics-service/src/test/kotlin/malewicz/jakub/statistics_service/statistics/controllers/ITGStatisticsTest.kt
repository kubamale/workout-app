package malewicz.jakub.statistics_service.statistics.controllers

import malewicz.jakub.statistics_service.TestcontainersConfiguration
import malewicz.jakub.statistics_service.clients.WorkoutClient
import malewicz.jakub.statistics_service.measurements.entities.MeasurementEntity
import malewicz.jakub.statistics_service.measurements.repositories.MeasurementRepository
import malewicz.jakub.statistics_service.statistics.dtos.OverallStatistics
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import malewicz.jakub.statistics_service.workouts.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyList
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime
import java.util.*


@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@Sql(scripts = ["/scripts/clear.sql", "/scripts/init.sql"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGStatisticsTest(
  @Autowired private val restTemplate: TestRestTemplate,
  @Autowired private val workoutRepository: WorkoutRepository,
  @Autowired private val measurementRepository: MeasurementRepository
) {
  @MockitoBean
  private lateinit var workoutClient: WorkoutClient
  private val userId = UUID.fromString("ad78bab6-2b61-471e-ae9e-e70a4ccb242b")

  @Test
  fun `get overall statistics should return 200 and statistics`() {
    val workout = workoutRepository.save(UserWorkoutEntity(null, userId, UUID.randomUUID(), LocalDateTime.now()))
    measurementRepository.save(MeasurementEntity(null, userId, LocalDateTime.now(), 77.0))
    `when`(workoutClient.findAllByIds(anyList())).thenReturn(listOf(WorkoutBasicInfo(workout.id!!, "legs")))
    val httpEntity = HttpEntity(null, getUserIdInHeader())
    val result =
      restTemplate.exchange("/api/v1/statistics/overall", HttpMethod.GET, httpEntity, OverallStatistics::class.java)
    assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(result.body).isNotNull
    assertThat(result.body!!.daysSinceWorkout).isEqualTo(0)
    assertThat(result.body!!.thisWeeksWorkouts).hasSize(1)
    assertThat(result.body!!.thirtyDaysMeasurements.size).isGreaterThanOrEqualTo(1)
  }

  private fun getUserIdInHeader() = HttpHeaders().apply { set("X-User-Id", userId.toString()) }
}