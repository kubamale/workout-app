package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.TimeExerciseEntity
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import malewicz.jakub.statistics_service.sets.entities.TimeSetEntity
import malewicz.jakub.statistics_service.sets.repositories.TimeSetRepository
import org.springframework.stereotype.Service

@Service
class TimeExerciseService(private val timeSetRepository: TimeSetRepository) {

  fun createTimeStatistics(exercises: List<TimeExerciseEntity>): List<ExerciseStatistics> {
    val sets =
      timeSetRepository.findAllByExerciseIdIn(exercises.map { it.id!! }.toMutableSet()).groupBy { it.exercise!!.id!! }
    return exercises.map {
      val exerciseSets = sets[it.id] ?: emptyList()
      ExerciseStatistics(
        exerciseId = it.exerciseId,
        date = it.workout.date,
        maxWeight = getMaxWeight(exerciseSets),
        time = getMaxTime(exerciseSets),
      )
    }
  }

  private fun getMaxWeight(sets: List<TimeSetEntity>) = sets.maxOfOrNull { it.weight } ?: 0.0
  private fun getMaxTime(sets: List<TimeSetEntity>) = sets.maxOfOrNull { it.time } ?: 0
}