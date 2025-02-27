package malewicz.jakub.exercise_service.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.ExerciseDetails
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.dtos.PageableResponse
import malewicz.jakub.exercise_service.entities.Equipment
import malewicz.jakub.exercise_service.entities.ExerciseType
import malewicz.jakub.exercise_service.entities.MuscleGroup
import malewicz.jakub.exercise_service.exceptions.BadRequestException
import malewicz.jakub.exercise_service.exceptions.ResourceNotFoundException
import malewicz.jakub.exercise_service.services.ExerciseService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ExerciseController::class])
class ExerciseControllerTest(
  @Autowired private val mockMvc: MockMvc,
  @Autowired private val objectMapper: ObjectMapper
) {

  @MockitoBean
  private lateinit var exerciseService: ExerciseService

  @Test
  fun `get all exercises should return 200 and pageable response`() {
    val filters = listOf(FilterRequest("STRENGTH", "type"))
    val pageable = PageRequest.of(0, 10)
    val exercise = ExerciseBasicsResponse(UUID.randomUUID(), "Legs", "url")
    val response = PageableResponse(10, 10, 10, false, 10, listOf(exercise))
    `when`(exerciseService.getAllExercises(pageable, filters)).thenReturn(response)

    mockMvc
      .perform(
        post("/api/v1/exercises")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(filters))
          .param("page", "0")
          .param("size", "10")
      )
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(response)))
  }

  @Test
  fun `get all exercises should return 400 when exercise service throws BadRequestException`() {
    val filters = listOf(FilterRequest("STRENGTH", "type"))
    val pageable = PageRequest.of(0, 10)
    `when`(exerciseService.getAllExercises(pageable, filters))
      .thenThrow(BadRequestException::class.java)

    mockMvc
      .perform(
        post("/api/v1/exercises")
          .contentType(MediaType.APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(filters))
          .param("page", "0")
          .param("size", "10")
      )
      .andExpect(status().isBadRequest)
      .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `get exercise details details returns 404 when exercise service throws ResourceNotFoundException`() {
    val id = UUID.randomUUID()
    `when`(exerciseService.getDetails(id)).thenThrow(ResourceNotFoundException::class.java)
    mockMvc
      .perform(get("/api/v1/exercises/$id"))
      .andExpect(status().isNotFound)
      .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `get exercise details details returns 200 and details of exercise by id`() {
    val id = UUID.randomUUID()
    val exercise =
      ExerciseDetails(
        id, "Legs", MuscleGroup.LATS, "desc", ExerciseType.STRETCHING, Equipment.NONE
      )
    `when`(exerciseService.getDetails(id)).thenReturn(exercise)
    mockMvc
      .perform(get("/api/v1/exercises/$id"))
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(exercise)))
  }

  @Test
  fun `get all exercises by id should return 200 and list of exercises`() {
    val exercise1 = ExerciseDetails(
      UUID.randomUUID(), "Legs", MuscleGroup.LATS, "desc", ExerciseType.STRETCHING, Equipment.NONE
    )
    val exercise2 = ExerciseDetails(
      UUID.randomUUID(), "Legs", MuscleGroup.LATS, "desc", ExerciseType.STRETCHING, Equipment.NONE
    )
    val ids = listOf(exercise1.id, exercise2.id)
    `when`(exerciseService.getAllByIds(ids)).thenReturn(listOf(exercise1, exercise2))

    mockMvc.perform(
      get("/api/v1/exercises").param("ids", ids.joinToString(","))
    )
      .andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().json(objectMapper.writeValueAsString(listOf(exercise1, exercise2))))
  }
}
