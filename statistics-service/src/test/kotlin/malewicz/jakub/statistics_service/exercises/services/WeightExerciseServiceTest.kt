package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.WeightExerciseEntity
import malewicz.jakub.statistics_service.sets.entities.WeightSetEntity
import malewicz.jakub.statistics_service.sets.repositories.WeightSetRepository
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
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
class WeightExerciseServiceTest {

  @Mock
  private lateinit var weightSetRepository: WeightSetRepository

  @InjectMocks
  private lateinit var weightExerciseService: WeightExerciseService

  @Test
  fun `create weight statistics should return list of statistics for exercise `() {
    val workout = UserWorkoutEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now())
    val exerciseId = UUID.randomUUID()
    val weightExercises = listOf(
      WeightExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      ),
      WeightExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      )
    )
    val sets = mutableListOf(
      WeightSetEntity(
        UUID.randomUUID(),
        10,
        10.0
      ).apply { exercise = weightExercises[0] },
      WeightSetEntity(
        UUID.randomUUID(),
        20,
        12.0
      ).apply { exercise = weightExercises[1] }
    )

    `when`(weightSetRepository.findAllByExerciseIdIn(weightExercises.mapNotNull { it.id }.toMutableSet())).thenReturn(
      sets
    )

    val result = weightExerciseService.createWeightStatistics(weightExercises)
    assertThat(result).hasSize(2)
    result.forEach { assertThat(it.exerciseId).isEqualTo(exerciseId) }
    val volumes = result.map(ExerciseStatistics::volume)
    assertThat(volumes).containsAll(sets.map { it.weight * it.reps })
  }
}