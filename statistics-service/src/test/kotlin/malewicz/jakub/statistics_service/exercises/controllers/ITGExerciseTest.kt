package malewicz.jakub.statistics_service.exercises.controllers

import malewicz.jakub.statistics_service.TestcontainersConfiguration
import malewicz.jakub.statistics_service.clients.ExerciseClient
import malewicz.jakub.statistics_service.exercises.dtos.ExerciseBasicInfo
import org.assertj.core.api.Assertions.assertThat
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
import java.util.*
import kotlin.test.Test

@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@Sql(scripts = ["/scripts/clear.sql", "/scripts/init.sql"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGExerciseTest(
  @Autowired private val restTemplate: TestRestTemplate,
) {

  @MockitoBean
  private lateinit var exerciseClient: ExerciseClient

  val exerciseIds = listOf(
    UUID.fromString("77168257-6637-4098-87e5-5adf0e18fb5b"),
    UUID.fromString("8dd7c09c-2135-4002-beca-aa070b9fb463"),
    UUID.fromString("b123861c-965b-41eb-8a55-11e94758f71f")
  )

  val userId = UUID.fromString("ad78bab6-2b61-471e-ae9e-e70a4ccb242b")

  @Test
  fun `get users completed exercises should return 200 and list of exercises`() {
    val exercisesInfo = listOf(
      ExerciseBasicInfo(exerciseIds[0], "legs", "url"),
      ExerciseBasicInfo(exerciseIds[1], "arms", "url"),
      ExerciseBasicInfo(exerciseIds[2], "abs", "url")
    )
    `when`(exerciseClient.getBasicExercisesInformation(exerciseIds)).thenReturn(exercisesInfo)

    val httpEntity = HttpEntity(null, getUserIdInHeader())
    val result =
      restTemplate.exchange("/api/v1/exercises", HttpMethod.GET, httpEntity, Array<ExerciseBasicInfo>::class.java)
    assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(result.body).isNotNull
    assertThat(result.body!!).hasSize(3)
    result.body!!.forEach { exercise ->
      assertThat(exercise.id).isIn(exerciseIds)
    }
  }

  private fun getUserIdInHeader() = HttpHeaders().apply { set("X-User-Id", userId.toString()) }
}