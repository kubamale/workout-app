package malewicz.jakub.statistics_service.workouts.mappers

import malewicz.jakub.statistics_service.workouts.dtos.ExerciseStatisticsDto
import malewicz.jakub.statistics_service.workouts.dtos.SetType
import malewicz.jakub.statistics_service.workouts.entities.*
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring", uses = [SetMapper::class])
abstract class ExerciseMapper {

  private val setMapper: SetMapper = Mappers.getMapper(SetMapper::class.java)

  fun toExerciseStatisticsEntity(
    exercise: ExerciseStatisticsDto,
    workout: UserWorkoutEntity
  ): ExerciseEntity {
    return when (exercise.type) {
      SetType.TIME -> TimeExerciseEntity(
        exercise.exerciseId,
        exercise.workoutExerciseId,
        workout,
        setMapper.toTimeSetEntity(exercise.sets)
      ).apply { sets.forEach { it.exercise = this } }

      SetType.WEIGHT -> WeightExerciseEntity(
        exercise.exerciseId,
        exercise.workoutExerciseId,
        workout,
        setMapper.toWeightSetEntity(exercise.sets)
      ).apply { sets.forEach { it.exercise = this } }

      SetType.DISTANCE -> DistanceExerciseEntity(
        exercise.exerciseId,
        exercise.workoutExerciseId,
        workout,
        setMapper.toDistanceSetEntity(exercise.sets)
      ).apply { sets.forEach { it.exercise = this } }
    }
  }
}