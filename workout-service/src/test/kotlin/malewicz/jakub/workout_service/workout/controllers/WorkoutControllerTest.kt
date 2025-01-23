package malewicz.jakub.workout_service.workout.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.services.WorkoutService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [WorkoutController::class])
class WorkoutControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {

    @MockitoBean
    private lateinit var workoutService: WorkoutService


    @Test
    fun `create workout returns 201 and workout id when passed correct data`() {
        val createRequest = WorkoutCreateRequest("push")
        val userId = UUID.randomUUID()
        `when`(workoutService.createWorkout(createRequest, userId)).thenReturn(UUID.randomUUID())
        mockMvc.perform(
            post("/api/v1/workout").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)).header("X-User-Id", userId)
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
    }

    @Test
    fun `create workout returns 400 when X-User-Id header not passed`() {
        val createRequest = WorkoutCreateRequest("push")
        mockMvc.perform(
            post("/api/v1/workout").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `create workout returns 400 when passed empty workout name`() {
        val createRequest = WorkoutCreateRequest("")
        val userId = UUID.randomUUID()
        mockMvc.perform(
            post("/api/v1/workout").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)).header("X-User-Id", userId)
        )
            .andExpect(status().isBadRequest)
    }
}