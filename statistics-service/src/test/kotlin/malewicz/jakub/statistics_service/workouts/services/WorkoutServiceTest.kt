package malewicz.jakub.statistics_service.workouts.services

import malewicz.jakub.statistics_service.clients.WorkoutClient
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import malewicz.jakub.statistics_service.workouts.mappers.WorkoutMapper
import malewicz.jakub.statistics_service.workouts.repositories.WorkoutRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class WorkoutServiceTest {

  @Mock
  private lateinit var workoutRepository: WorkoutRepository

  @Mock
  private lateinit var workoutMapper: WorkoutMapper

  @Mock
  private lateinit var workoutClient: WorkoutClient

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

  @Test
  fun `get days since last workout should return a number of days passed`() {
    val userId = UUID.randomUUID()
    `when`(workoutRepository.getLatestWorkoutDate(userId)).thenReturn(LocalDateTime.now().minusDays(1))
    val days = workoutService.getDaysSinceLastWorkout(userId)
    assertEquals(1, days)
  }

  @Test
  fun `get days since last workout should return 0 when no workouts were found`() {
    val userId = UUID.randomUUID()
    `when`(workoutRepository.getLatestWorkoutDate(userId)).thenReturn(null)
    val days = workoutService.getDaysSinceLastWorkout(userId)
    assertEquals(0, days)
  }

  @Test
  fun `get workouts from current week should return empty list when no workouts were found`() {
    val userId = UUID.randomUUID()
    `when`(workoutRepository.getAllByUserIdAndDateGreaterThan(any(), any())).thenReturn(listOf())
    val workouts = workoutService.getWorkoutsFromCurrentWeek(userId)
    assertThat(workouts.size).isEqualTo(0)
  }

  @Test
  fun `get workouts from current week should return workouts`() {
    val userId = UUID.randomUUID()
    val userWorkout = UserWorkoutEntity(UUID.randomUUID(), userId, UUID.randomUUID(), LocalDateTime.now().minusDays(1))
    `when`(workoutRepository.getAllByUserIdAndDateGreaterThan(any(), any())).thenReturn(listOf(userWorkout))
    `when`(workoutClient.findAllByIds(listOf(userWorkout.workoutId))).thenReturn(
      listOf(
        WorkoutBasicInfo(
          UUID.randomUUID(),
          "legs"
        )
      )
    )
    val workouts = workoutService.getWorkoutsFromCurrentWeek(userId)
    assertThat(workouts.size).isEqualTo(1)
  }
}