package malewicz.jakub.workout_service.workout.services

import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.mappers.WorkoutMapper
import malewicz.jakub.workout_service.workout.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
class WorkoutServiceTest {

    @Mock
    private lateinit var workoutRepository: WorkoutRepository
    @Mock
    private lateinit var workoutMapper: WorkoutMapper

    @InjectMocks
    private lateinit var workoutService: WorkoutService


    @Test
    fun `create workout returns workout id when passed correct data`() {
        val workout = WorkoutCreateRequest("push")
        val userId = UUID.randomUUID()
        val savedWorkout = WorkoutEntity(UUID.randomUUID(), "push", userId)
        `when`(workoutRepository.save(any())).thenReturn(savedWorkout)

        val result = workoutService.createWorkout(workout, userId)
        assertThat(result).isNotNull
        assertThat(result).isEqualTo(savedWorkout.id)
    }

    @Test
    fun `get user workouts returns workouts`() {
        val userId = UUID.randomUUID()
        `when`(workoutRepository.findByUserId(userId)).thenReturn(
            listOf(
                WorkoutEntity(UUID.randomUUID(), "push", userId),
                WorkoutEntity(UUID.randomUUID(), "pull", userId)
            )
        )
        `when`(workoutMapper.toWorkoutResponse(this.any(WorkoutEntity::class.java))).thenReturn(WorkoutResponse(UUID.randomUUID(), "Legs"), WorkoutResponse(UUID.randomUUID(), "Legs"))
        val result = workoutService.getUserWorkouts(userId)
        assertThat(result).isNotNull
        assertThat(result).hasSize(2)
    }

    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}