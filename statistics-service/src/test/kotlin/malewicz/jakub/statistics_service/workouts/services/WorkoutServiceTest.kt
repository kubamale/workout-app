package malewicz.jakub.statistics_service.workouts.services

import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import malewicz.jakub.statistics_service.workouts.mappers.WorkoutMapper
import malewicz.jakub.statistics_service.workouts.repositories.WorkoutRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
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
    fun `create workout should save workout`() {
        val userId = UUID.randomUUID()
        val request = WorkoutCreateRequest(workoutId = UUID.randomUUID())
        val workout = UserWorkoutEntity(
            userId = userId,
            workoutId = request.workoutId,
            date = LocalDateTime.now()
        )
        `when`(workoutMapper.toUserWorkoutEntity(request, userId)).thenReturn(workout)
        workoutService.createWorkout(request, userId)
        verify(workoutRepository).save(workout)
    }
}