package malewicz.jakub.statistics_service.exercises.services

import malewicz.jakub.statistics_service.exercises.entities.DistanceExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.ExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.TimeExerciseEntity
import malewicz.jakub.statistics_service.exercises.entities.WeightExerciseEntity
import malewicz.jakub.statistics_service.exercises.repositories.ExerciseRepository
import malewicz.jakub.statistics_service.statistics.dtos.ExerciseStatistics
import malewicz.jakub.statistics_service.sets.dtos.SetType
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExerciseService(
  private val exerciseRepository: ExerciseRepository,
  private val weightExerciseService: WeightExerciseService,
  private val timeExerciseService: TimeExerciseService,
  private val distanceExerciseService: DistanceExerciseService
) {
  fun getStatisticsForExercises(exercises: List<ExerciseEntity>): Map<SetType, List<ExerciseStatistics>> {
    val exerciseStatistics = HashMap<SetType, List<ExerciseStatistics>>()
    exerciseStatistics[SetType.WEIGHT] =
      weightExerciseService.createWeightStatistics(exercises.filterIsInstance<WeightExerciseEntity>())
    exerciseStatistics[SetType.TIME] =
      timeExerciseService.createTimeStatistics(exercises.filterIsInstance<TimeExerciseEntity>())
    exerciseStatistics[SetType.DISTANCE] =
      distanceExerciseService.createDistanceStatistics(exercises.filterIsInstance<DistanceExerciseEntity>())
    return exerciseStatistics
  }

  fun getExercisesForUser(userId: UUID, exerciseId: UUID) =
    exerciseRepository.findAllByExerciseIdAndWorkoutUserId(exerciseId, userId)
}