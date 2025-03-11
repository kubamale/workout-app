package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.DistanceExerciseEntity
import malewicz.jakub.statistics_service.sets.entities.DistanceSetEntity
import malewicz.jakub.statistics_service.sets.repositories.DistanceSetRepository
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
class DistanceExerciseServiceTest {

  @Mock
  private lateinit var distanceSetRepository: DistanceSetRepository

  @InjectMocks
  private lateinit var distanceExerciseService: DistanceExerciseService

  @Test
  fun `create distance statistics should return distance statistics`() {
    val workout = UserWorkoutEntity(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDateTime.now())
    val exerciseId = UUID.randomUUID()
    val distanceExercises = listOf(
      DistanceExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      ),
      DistanceExerciseEntity(
        UUID.randomUUID(),
        exerciseId,
        UUID.randomUUID(),
        workout
      )
    )
    val sets = mutableListOf(
      DistanceSetEntity(
        UUID.randomUUID(),
        12.0
      ).apply { exercise = distanceExercises[1] },
      DistanceSetEntity(
        UUID.randomUUID(),
        10.0
      ).apply { exercise = distanceExercises[1] }
    )

    `when`(distanceSetRepository.findAllByExerciseIdIn(distanceExercises.mapNotNull { it.id }
      .toMutableSet())).thenReturn(
      sets
    )

    val result = distanceExerciseService.createDistanceStatistics(distanceExercises)
    assertThat(result).hasSize(2)
    result.forEach { assertThat(it.exerciseId).isEqualTo(exerciseId) }
    val distances = result.map { it.distance }
    assertThat(distances).containsAll(listOf(12.0, 0.0))
  }
}