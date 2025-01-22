package malewicz.jakub.workout_service.exercise.controllers

import malewicz.jakub.workout_service.exceptions.ResourceNotFoundException
import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import malewicz.jakub.workout_service.exercise.services.ExerciseService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@WebMvcTest(controllers = [ExerciseController::class])
class ExerciseControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {

    @MockitoBean
    private lateinit var exerciseService: ExerciseService

    @Test
    fun `getDetails should return exercise details when passed correct exercise id`() {
        val id = UUID.randomUUID()
        val exercise =
            ExerciseDetails(id, "Crunch", MuscleGroup.ABDOMINALS, "description", ExerciseType.STRENGTH, Equipment.NONE)
        `when`(exerciseService.getExerciseDetails(id)).thenReturn(exercise)
        mockMvc.perform(get("/api/v1/exercise/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(id.toString()))
    }

    @Test
    fun `getDetails should return 404 when passed not existing exercise id`() {
        val id = UUID.randomUUID()
        `when`(exerciseService.getExerciseDetails(id)).thenThrow(ResourceNotFoundException("Exercise not found"))
        mockMvc.perform(get("/api/v1/exercise/{id}", id))
            .andExpect(status().isNotFound)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }
}