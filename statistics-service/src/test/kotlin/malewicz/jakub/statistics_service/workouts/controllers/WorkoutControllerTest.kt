package malewicz.jakub.statistics_service.workouts.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.statistics_service.conversion.LengthUnits
import malewicz.jakub.statistics_service.conversion.SetUnitsConverter
import malewicz.jakub.statistics_service.conversion.WeightUnits
import malewicz.jakub.statistics_service.workouts.dtos.ExerciseStatisticsDto
import malewicz.jakub.statistics_service.workouts.dtos.SetDto
import malewicz.jakub.statistics_service.workouts.dtos.SetType
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [WorkoutController::class])
class WorkoutControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
) {

    @MockitoBean
    private lateinit var workoutService: WorkoutService

    @MockitoBean
    private lateinit var setUnitsConverter: SetUnitsConverter

    @Test
    fun `add workout should return 201 when saved workout`() {
        val setDto = SetDto(
            reps = 10, weight = 12.0, setType = SetType.WEIGHT,
            time = null,
            distance = null
        )
        val request = WorkoutCreateRequest(
            workoutId = UUID.randomUUID(),
            exercises = mutableListOf(
                ExerciseStatisticsDto(
                    exerciseId = UUID.randomUUID(),
                    workoutExerciseId = UUID.randomUUID(),
                    sets = mutableListOf(
                        setDto
                    )
                )
            )
        )

        `when`(
            setUnitsConverter.convertSetUnits(
                setDto,
                WeightUnits.KG,
                WeightUnits.KG,
                LengthUnits.CM,
                LengthUnits.CM
            )
        ).thenReturn(setDto)

        mockMvc.perform(
            post("/api/v1/workout").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)).header("X-User-Id", UUID.randomUUID())
                .header("X-Weight-Units", WeightUnits.KG).header("X-Length-Units", LengthUnits.CM)
        ).andExpect(status().isCreated)
    }
}