package malewicz.jakub.workout_service.workout.controllers

import malewicz.jakub.workout_service.clients.ExerciseClient
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyList
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*

@Sql(scripts = ["/scripts/clear.sql", "/scripts/init.sql"])
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGWorkoutTest(
  @Autowired val restTemplate: TestRestTemplate
) {
  @MockitoBean
  private lateinit var exerciseClient: ExerciseClient
  private val userId = UUID.fromString("e00e52c7-f272-41a3-b6f3-ff16867f874a")
  private val workoutId = UUID.fromString("837e92ab-8efc-4db7-9f18-bd53e81d9a5e")

  companion object {
    val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:latest")

    @BeforeAll
    @JvmStatic
    fun startDBContainer() {
      postgres.start()
    }

    @AfterAll
    @JvmStatic
    fun stopDBContainer() {
      postgres.stop()
    }
  }


  @Test
  fun `test containers`() {
    assertThat(postgres.isRunning).isTrue()
  }

  @Test
  fun `get user workouts`() {
    val httpEntity = HttpEntity(null, getUserIdInHeader())
    val res =
      restTemplate.exchange("/api/v1/workout/all", HttpMethod.GET, httpEntity, Array<WorkoutResponse>::class.java)
    assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(res.body).isNotNull
    assertThat(res.body?.size).isEqualTo(2)
  }

  @Test
  fun `get workout by id`() {
    `when`(exerciseClient.getExercisesDetails(anyList())).thenReturn(listOf())

    val httpEntity = HttpEntity(null, getUserIdInHeader())
    val res = restTemplate.exchange(
      "/api/v1/workout/$workoutId",
      HttpMethod.GET,
      httpEntity,
      WorkoutDetailsResponse::class.java
    )

    assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(res.body).isNotNull
    assertThat(res.body?.id).isEqualTo(workoutId)
    assertThat(res.body?.exercises).hasSize(3)
  }

  @Test
  fun `create workout`() {
    val httpEntity = HttpEntity(WorkoutCreateRequest("Push-Pull"), getUserIdInHeader())
    val res = restTemplate.exchange("/api/v1/workout", HttpMethod.POST, httpEntity, UUID::class.java)
    assertThat(res.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(res.body).isNotNull
  }

  private fun getUserIdInHeader() = HttpHeaders().apply { set("X-User-Id", userId.toString()) }
}