package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.WeightExerciseEntity
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import malewicz.jakub.statistics_service.sets.entities.WeightSetEntity
import malewicz.jakub.statistics_service.sets.repositories.WeightSetRepository
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service
class WeightExerciseService(private val weightSetRepository: WeightSetRepository) {

  fun createWeightStatistics(exercises: List<WeightExerciseEntity>): List<ExerciseStatistics> {
    val sets =
      weightSetRepository.findAllByExerciseIdIn(exercises.map { it.id!! }.toMutableSet()).groupBy { it.exercise!!.id!! }

    return exercises.map {
      val exercisesSets = sets[it.id] ?: emptyList()
      val setWithMaxWeight = getSetWithMaxWeight(exercisesSets)
      return@map ExerciseStatistics(
        exerciseId = it.exerciseId,
        date = it.workout.date,
        volume = getVolume(exercisesSets),
        oneRepMax = getOneRepMax(exercisesSets),
        maxWeight = setWithMaxWeight?.weight ?: 0.0,
        reps = setWithMaxWeight?.reps ?: 0
      )
    }
  }

  private fun getVolume(sets: List<WeightSetEntity>) = sets.sumOf { it.weight * it.reps }

  private fun getOneRepMax(sets: List<WeightSetEntity>) =
    sets.maxOfOrNull { calculateOneRepMax(it.weight, it.reps) } ?: 0.0

  private fun getSetWithMaxWeight(sets: List<WeightSetEntity>) = sets.maxByOrNull { it.weight }

  private fun calculateOneRepMax(weight: Double, reps: Int): Double {
    val oneRepMaxByOConner = oneRepMaxByOConnerFormula(weight, reps)
    val oneRepMaxByEpley = oneRepMaxByEpleyFormula(weight, reps)
    return ((oneRepMaxByEpley + oneRepMaxByOConner) / 2)
      .toBigDecimal()
      .setScale(2, RoundingMode.HALF_UP)
      .toDouble()
  }


  private fun oneRepMaxByOConnerFormula(weight: Double, reps: Int) = weight * (1.0 + (0.025 * reps))
  private fun oneRepMaxByEpleyFormula(weight: Double, reps: Int) = weight * (1.0 + (0.0333 * reps))
}