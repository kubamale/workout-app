package malewicz.jakub.exercise_service.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.dtos.PageableResponse
import malewicz.jakub.exercise_service.exceptions.BadRequestException
import malewicz.jakub.exercise_service.services.ExerciseService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

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

        mockMvc.perform(
            post("/api/v1/exercises").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)).param("page", "0").param("size", "10")
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(response)))
    }

    @Test
    fun `get all exercises should return 400 when exercise service throws BadRequestException`() {
        val filters = listOf(FilterRequest("STRENGTH", "type"))
        val pageable = PageRequest.of(0, 10)
        `when`(exerciseService.getAllExercises(pageable, filters)).thenThrow(BadRequestException::class.java)

        mockMvc.perform(
            post("/api/v1/exercises").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(filters)).param("page", "0").param("size", "10")
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


}