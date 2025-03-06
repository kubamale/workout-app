package malewicz.jakub.workout_service.exercise.controllers

import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseReorderRequest
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.SetType
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.set.repositories.SetRepository
import malewicz.jakub.workout_service.workout.entities.DistanceWorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.entities.TimeWorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.entities.WeightWorkoutExerciseEntity
import malewicz.jakub.workout_service.workout.repositories.WorkoutExerciseRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*
import kotlin.test.Test

@Sql(scripts = ["/scripts/clear.sql", "/scripts/init.sql"])
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGExerciseTest(
  @Autowired val restTemplate: TestRestTemplate,
  @Autowired val workoutExerciseRepository: WorkoutExerciseRepository,
  @Autowired val setRepository: SetRepository
) {
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
  fun `test container`() {
    assertThat(postgres.isRunning).isTrue()
  }

  @Test
  fun `add weight exercise to workout`() {
    val requestBody = ExerciseCreateRequest(
      workoutId = workoutId,
      exerciseId = UUID.randomUUID(),
      exerciseType = SetType.WEIGHT,
      order = 1,
      sets = mutableListOf(
        WeightSetCreateRequest(
          null,
          0,
          10,
          20.0
        ),
        WeightSetCreateRequest(
          null,
          1,
          10,
          20.0
        ),
      )
    )

    val httpEntity = HttpEntity(requestBody, getUserIdInHeader())
    val response = restTemplate.exchange("/api/v1/exercise/weight", HttpMethod.POST, httpEntity, UUID::class.java)
    assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(response.body).isNotNull()
    val workoutExerciseId = response.body!!
    val workoutExercise = workoutExerciseRepository.findById(workoutExerciseId).orElseThrow()
    assertThat(workoutExercise).isInstanceOf(WeightWorkoutExerciseEntity::class.java)
    val sets = setRepository.findAllByWorkoutExerciseId(workoutExerciseId)
    assertThat(sets).hasSize(2)
  }

  @Test
  fun `add time exercise to workout`() {
    val requestBody = ExerciseCreateRequest(
      workoutId = workoutId,
      exerciseId = UUID.randomUUID(),
      exerciseType = SetType.TIME,
      order = 1,
      sets = mutableListOf(
        TimeSetCreateRequest(
          null,
          0,
          10,
          20.0
        ),
        TimeSetCreateRequest(
          null,
          1,
          10,
          20.0
        ),
      )
    )

    val httpEntity = HttpEntity(requestBody, getUserIdInHeader())
    val response = restTemplate.exchange("/api/v1/exercise/time", HttpMethod.POST, httpEntity, UUID::class.java)
    assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(response.body).isNotNull()
    val workoutExerciseId = response.body!!
    val workoutExercise = workoutExerciseRepository.findById(workoutExerciseId).orElseThrow()
    assertThat(workoutExercise).isInstanceOf(TimeWorkoutExerciseEntity::class.java)
    val sets = setRepository.findAllByWorkoutExerciseId(workoutExerciseId)
    assertThat(sets).hasSize(2)
  }

  @Test
  fun `add distance exercise to workout`() {
    val requestBody = ExerciseCreateRequest(
      workoutId = workoutId,
      exerciseId = UUID.randomUUID(),
      exerciseType = SetType.DISTANCE,
      order = 1,
      sets = mutableListOf(
        DistanceSetCreateRequest(
          null,
          0,
          10.0
        ),
        DistanceSetCreateRequest(
          null,
          1,
          23.0
        ),
      )
    )

    val httpEntity = HttpEntity(requestBody, getUserIdInHeader())
    val response = restTemplate.exchange("/api/v1/exercise/distance", HttpMethod.POST, httpEntity, UUID::class.java)
    assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(response.body).isNotNull()
    val workoutExerciseId = response.body!!
    val workoutExercise = workoutExerciseRepository.findById(workoutExerciseId).orElseThrow()
    assertThat(workoutExercise).isInstanceOf(DistanceWorkoutExerciseEntity::class.java)
    val sets = setRepository.findAllByWorkoutExerciseId(workoutExerciseId)
    assertThat(sets).hasSize(2)
  }

  @Test
  fun `reorder exercises in workout`() {
    val exercises = workoutExerciseRepository.findAll().filter { it.workout.id!! == workoutId }
      .sortedBy { it.exerciseOrder }
    val newOrder =
      exercises.mapIndexed { index, workoutExercise -> workoutExercise.id!! to exercises.size - index - 1 }.toMap()
    val httpEntity = HttpEntity(ExerciseReorderRequest(workoutId, newOrder), getUserIdInHeader())
    val res = restTemplate.exchange("/api/v1/exercise/order", HttpMethod.PATCH, httpEntity, Unit::class.java)

    assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
    val updatedExercises = workoutExerciseRepository.findAll().filter { it.workout.id!! == workoutId }
    updatedExercises.forEach {
      assertThat(it.exerciseOrder).isEqualTo(newOrder[it.id])
    }
  }

  @Test
  fun `delete exercises in workout`() {
    val exercise = workoutExerciseRepository.findAll().first { it.workout.id!! == workoutId }
    val res = restTemplate.exchange(
      "/api/v1/exercise/${exercise.id}?workoutId=$workoutId",
      HttpMethod.DELETE,
      null,
      Unit::class.java
    )

    assertThat(res.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(workoutExerciseRepository.findById(exercise.id!!)).isEmpty
    assertThat(setRepository.findAllByWorkoutExerciseId(exercise.id!!)).isEmpty()
  }

  private fun getUserIdInHeader() = HttpHeaders().apply { set("X-User-Id", userId.toString()) }
}