package malewicz.jakub.statistics_service.workouts.controllers

import malewicz.jakub.statistics_service.TestcontainersConfiguration
import malewicz.jakub.statistics_service.workouts.dtos.ExerciseStatisticsDto
import malewicz.jakub.statistics_service.workouts.dtos.SetDto
import malewicz.jakub.statistics_service.workouts.dtos.SetType
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.util.*
import kotlin.test.Test


@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@Sql(scripts = ["/scripts/clear.sql", "/scripts/init.sql"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGWorkoutTest(
  @Autowired private val restTemplate: TestRestTemplate,
  @Autowired private val workoutRepository: WorkoutRepository,
) {
  private val userId = UUID.fromString("ad78bab6-2b61-471e-ae9e-e70a4ccb242b")

  @Test
  fun `add workout record should save new workout`() {
    val initialWorkoutsAmount = workoutRepository.count()
    val requestBody = WorkoutCreateRequest(
      UUID.randomUUID(), mutableListOf(
        ExerciseStatisticsDto(
          UUID.randomUUID(),
          UUID.randomUUID(),
          SetType.WEIGHT,
          mutableListOf(
            SetDto(10, 20.0, null,null),
            SetDto(8, 30.0, null, null)
          )
        )
      )
    )

    val httpEntity = HttpEntity(requestBody, getUserIdInHeader())
    val result = restTemplate.exchange("/api/v1/workout", HttpMethod.POST, httpEntity, Unit::class.java)

    assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(workoutRepository.count()).isEqualTo(initialWorkoutsAmount + 1)
  }

  private fun getUserIdInHeader() = HttpHeaders().apply { set("X-User-Id", userId.toString()) }

}