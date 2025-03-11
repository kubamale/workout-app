package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.TimeExerciseEntity
import malewicz.jakub.statistics_service.sets.entities.TimeSetEntity
import malewicz.jakub.statistics_service.sets.repositories.TimeSetRepository
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class TimeExerciseServiceTest {

  @Mock
  private lateinit var timeSetRepository: TimeSetRepository

  @InjectMocks
  private lateinit var timeExerciseService: TimeExerciseService

  @Test
  fun `create time statistics should return list of statistics `() {
    val workout = UserWorkoutEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now())
    val exerciseId = UUID.randomUUID()
    val timeExercises = listOf(
      TimeExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      ),
      TimeExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      )
    )
    val sets = mutableListOf(
      TimeSetEntity(
        UUID.randomUUID(),
        20,
        12.0
      ).apply { exercise = timeExercises[1] },
      TimeSetEntity(
        UUID.randomUUID(),
        300,
        10.0
      ).apply { exercise = timeExercises[1] }
    )

    `when`(timeSetRepository.findAllByExerciseIdIn(timeExercises.mapNotNull { it.id }.toMutableSet())).thenReturn(
      sets
    )

    val result = timeExerciseService.createTimeStatistics(timeExercises)
    assertThat(result).hasSize(2)
    result.forEach { assertThat(it.exerciseId).isEqualTo(exerciseId) }
    val time = result.map { it.time }
    assertThat(time).containsAll(listOf(300, 0))
    val weights = result.map { it.maxWeight }
    assertThat(weights).containsAll(listOf(12.0, 0.0))
  }
}