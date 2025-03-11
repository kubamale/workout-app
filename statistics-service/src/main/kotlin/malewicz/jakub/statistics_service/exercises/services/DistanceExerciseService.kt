package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.DistanceExerciseEntity
import malewicz.jakub.statistics_service.sets.entities.DistanceSetEntity
import malewicz.jakub.statistics_service.sets.repositories.DistanceSetRepository
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import org.springframework.stereotype.Service

@Service
class DistanceExerciseService(private val distanceSetRepository: DistanceSetRepository) {

  fun createDistanceStatistics(exercises: List<DistanceExerciseEntity>): List<ExerciseStatistics> {
    val sets = distanceSetRepository.findAllByExerciseIdIn(exercises.map { it.id!! }.toMutableSet())
      .groupBy { it.exercise!!.id!! }
    return exercises.map {
      val exerciseSets = sets[it.id] ?: emptyList()
      ExerciseStatistics(
        exerciseId = it.exerciseId,
        date = it.workout.date,
        distance = getMaxDistance(exerciseSets),
      )
    }
  }

  private fun getMaxDistance(sets: List<DistanceSetEntity>) = sets.maxOfOrNull { it.distance } ?: 0.0
}