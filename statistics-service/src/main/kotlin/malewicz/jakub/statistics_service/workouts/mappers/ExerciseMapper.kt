package malewicz.jakub.statistics_service.workouts.mappers

import malewicz.jakub.statistics_service.workouts.dtos.ExerciseStatisticsDto
import malewicz.jakub.statistics_service.workouts.entities.ExerciseStatisticsEntity
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring", uses = [SetMapper::class])
abstract class ExerciseMapper {

    private val setMapper: SetMapper = Mappers.getMapper(SetMapper::class.java)

    fun toExerciseStatisticsEntity(
        exercise: ExerciseStatisticsDto,
        workout: UserWorkoutEntity
    ): ExerciseStatisticsEntity {
        val exerciseEntity = ExerciseStatisticsEntity(
            exerciseId = exercise.exerciseId,
            workoutExerciseId = exercise.workoutExerciseId,
            workout = workout,
            sets = exercise.sets.map { setMapper.toSetEntity(it) }.toMutableList()
        )
        exerciseEntity.sets.forEach { it.exerciseStatistic = exerciseEntity }
        return exerciseEntity
    }
}