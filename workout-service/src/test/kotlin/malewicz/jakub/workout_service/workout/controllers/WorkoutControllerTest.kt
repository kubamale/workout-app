package malewicz.jakub.workout_service.workout.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import malewicz.jakub.workout_service.set.dtos.SetResponse
import malewicz.jakub.workout_service.weight.WeightConverter
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.dtos.WorkoutExerciseDetails
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.services.WorkoutService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
    private lateinit var weightConverter: WeightConverter
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

    @Test
    fun `get user workouts returns 200 when workout service returns workouts`() {
        val userId = UUID.randomUUID()
        val result = listOf(WorkoutResponse(UUID.randomUUID(), "Legs"), WorkoutResponse(UUID.randomUUID(), "Legs1"))
        `when`(workoutService.getUserWorkouts(userId)).thenReturn(result)
        mockMvc.perform(get("/api/v1/workout/all").header("X-User-Id", userId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(result)))
    }

    @Test
    fun `get user workouts returns 400 when X-User-Id header not passed`() {
        mockMvc.perform(get("/api/v1/workout/all"))
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `get  workout details returns 200 when workout service returns workouts`() {
        val userId = UUID.randomUUID()
        val workoutId = UUID.randomUUID()
        val response = WorkoutDetailsResponse(
            workoutId, "Legs", mutableListOf(
                WorkoutExerciseDetails(
                    UUID.randomUUID(),
                    ExerciseDetails(
                        UUID.randomUUID(),
                        "biceps",
                        MuscleGroup.BICEPS,
                        "desc",
                        ExerciseType.STRENGTH,
                        Equipment.DUMBBELL
                    ),
                    0,
                    mutableListOf(
                        SetResponse(
                            UUID.randomUUID(),
                            0,
                            10,
                            20.0
                        )
                    )
                )
            )
        )

        `when`(workoutService.getWorkoutDetails(userId, workoutId)).thenReturn(response)
        mockMvc.perform(get("/api/v1/workout/$workoutId").header("X-User-Id", userId).header("X-Weight-Units", "KG"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(response)))
    }

    @Test
    fun `get  workout details returns 200 and converts weight to pounds when workout service returns workouts`() {
        val userId = UUID.randomUUID()
        val workoutId = UUID.randomUUID()
        val exerciseId = UUID.randomUUID()
        val exerciseDetailsId = UUID.randomUUID()
        val setId = UUID.randomUUID()
        val serviceResponse = WorkoutDetailsResponse(
            workoutId, "Legs", mutableListOf(
                WorkoutExerciseDetails(
                    exerciseId,
                    ExerciseDetails(
                        exerciseDetailsId,
                        "biceps",
                        MuscleGroup.BICEPS,
                        "desc",
                        ExerciseType.STRENGTH,
                        Equipment.DUMBBELL
                    ),
                    0,
                    mutableListOf(
                        SetResponse(
                            setId,
                            0,
                            10,
                            20.0
                        )
                    )
                )
            )
        )
        val response = WorkoutDetailsResponse(
            workoutId, "Legs", mutableListOf(
                WorkoutExerciseDetails(
                    exerciseId,
                    ExerciseDetails(
                        exerciseDetailsId,
                        "biceps",
                        MuscleGroup.BICEPS,
                        "desc",
                        ExerciseType.STRENGTH,
                        Equipment.DUMBBELL
                    ),
                    0,
                    mutableListOf(
                        SetResponse(
                            setId,
                            0,
                            10,
                            50.0
                        )
                    )
                )
            )
        )

        `when`(weightConverter.fromKilograms(20.0)).thenReturn(50.0)
        `when`(workoutService.getWorkoutDetails(userId, workoutId)).thenReturn(serviceResponse)
        mockMvc.perform(get("/api/v1/workout/$workoutId").header("X-User-Id", userId).header("X-Weight-Units", "LB"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(objectMapper.writeValueAsString(response)))
    }
}