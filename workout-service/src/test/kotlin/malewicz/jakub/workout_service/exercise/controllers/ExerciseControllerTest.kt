package malewicz.jakub.workout_service.exercise.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*
import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseReorderRequest
import malewicz.jakub.workout_service.exercise.services.ExerciseService
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [ExerciseController::class])
class ExerciseControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {
  @MockitoBean private lateinit var exerciseService: ExerciseService

  @Test
  fun `addWeightExerciseToWorkout should return 404 when exerciseService throws ResourceNotFoundException`() {
    val exerciseRequest =
        ExerciseCreateRequest<WeightSetCreateRequest>(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
        )
    val userId = UUID.randomUUID()
    `when`(exerciseService.addExerciseToWorkout(exerciseRequest, userId))
        .thenThrow(ResourceNotFoundException("Exercise not found"))
    mockMvc
        .perform(
            post("/api/v1/exercise/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId))
        .andExpect(status().isNotFound)
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `addWeightExerciseToWorkout should return 400 when passed incorrect exercise order`() {
    val exerciseRequest =
        ExerciseCreateRequest<WeightSetCreateRequest>(
            UUID.randomUUID(),
            UUID.randomUUID(),
            -1,
        )
    val userId = UUID.randomUUID()
    mockMvc
        .perform(
            post("/api/v1/exercise/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId))
        .andExpect(status().isBadRequest)
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `addWeightExerciseToWorkout should return 400 when passed incorrect set number`() {
    val exerciseRequest =
        ExerciseCreateRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            mutableListOf(WeightSetCreateRequest(null, -1, 1, 1.0)))
    val userId = UUID.randomUUID()
    mockMvc
        .perform(
            post("/api/v1/exercise/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId))
        .andExpect(status().isBadRequest)
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `addWeightExerciseToWorkout should return 400 when passed incorrect set reps amount`() {
    val exerciseRequest =
        ExerciseCreateRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            mutableListOf(WeightSetCreateRequest(null, 1, -1, 1.0)))
    val userId = UUID.randomUUID()
    mockMvc
        .perform(
            post("/api/v1/exercise/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId))
        .andExpect(status().isBadRequest)
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `addWeightExerciseToWorkout should return 400 when passed incorrect weight`() {
    val exerciseRequest =
        ExerciseCreateRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            mutableListOf(WeightSetCreateRequest(null, 1, 1, -1.0)))
    val userId = UUID.randomUUID()
    mockMvc
        .perform(
            post("/api/v1/exercise/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId))
        .andExpect(status().isBadRequest)
        .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
  }

  @Test
  fun `addWeightExerciseToWorkout should return 201 when passed correct data`() {
    val exerciseRequest =
        ExerciseCreateRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            mutableListOf(WeightSetCreateRequest(null, 1, 1, 1.0)))
    val userId = UUID.randomUUID()
    val exerciseId = UUID.randomUUID()
    `when`(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).thenReturn(exerciseId)
    mockMvc
        .perform(
            post("/api/v1/exercise/weight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId))
        .andExpect(status().isCreated)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(exerciseId)))
  }

  @Test
  fun `addDistanceExerciseToWorkout should return 201 when passed correct data`() {
    val exerciseRequest =
        ExerciseCreateRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            mutableListOf(DistanceSetCreateRequest(null, 1, 10.9)))
    val userId = UUID.randomUUID()
    val exerciseId = UUID.randomUUID()
    `when`(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).thenReturn(exerciseId)
    mockMvc
        .perform(
            post("/api/v1/exercise/distance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId)
                .header("X-Weight-Units", "KG"))
        .andExpect(status().isCreated)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(exerciseId)))
  }

  @Test
  fun `addTimeExerciseToWorkout should return 201 when passed correct data`() {
    val exerciseRequest =
        ExerciseCreateRequest(
            UUID.randomUUID(),
            UUID.randomUUID(),
            0,
            mutableListOf(TimeSetCreateRequest(null, 1, 10, 0.0)))
    val userId = UUID.randomUUID()
    val exerciseId = UUID.randomUUID()
    `when`(exerciseService.addExerciseToWorkout(exerciseRequest, userId)).thenReturn(exerciseId)
    mockMvc
        .perform(
            post("/api/v1/exercise/time")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(exerciseRequest))
                .header("X-User-Id", userId)
                .header("X-Weight-Units", "KG"))
        .andExpect(status().isCreated)
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().json(objectMapper.writeValueAsString(exerciseId)))
  }

  @Test
  fun `reorder exercises should return 200`() {
    val request = ExerciseReorderRequest(UUID.randomUUID(), mapOf(Pair(UUID.randomUUID(), 1)))
    val userId = UUID.randomUUID()
    mockMvc
        .perform(
            patch("/api/v1/exercise/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("X-User-Id", userId))
        .andExpect(status().isOk)
  }

  @Test
  fun `delete from workout returns 200`() {
    val exerciseId = UUID.randomUUID()
    val workoutId = UUID.randomUUID()
    mockMvc
        .perform(
            delete("/api/v1/exercise/{exerciseId}", exerciseId)
                .param("workoutId", workoutId.toString()))
        .andExpect(status().isOk)
  }
}
