package malewicz.jakub.statistics_service.workouts.mappers

import malewicz.jakub.statistics_service.exercises.mappers.ExerciseMapper
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import java.time.LocalDateTime
import java.util.UUID

@Mapper(componentModel = "spring", uses = [ExerciseMapper::class])
abstract class WorkoutMapper {

  private val exerciseMapper: ExerciseMapper = Mappers.getMapper(ExerciseMapper::class.java)

  fun toUserWorkoutEntity(workoutCreateRequest: WorkoutCreateRequest, userId: UUID): UserWorkoutEntity {
    val userWorkoutEntity = UserWorkoutEntity(
      userId = userId,
      workoutId = workoutCreateRequest.workoutId,
      date = LocalDateTime.now(),
    )

    userWorkoutEntity.exerciseEntity =
      workoutCreateRequest.exercises.map { exerciseMapper.toExerciseStatisticsEntity(it, userWorkoutEntity) }
        .toMutableList()

    return userWorkoutEntity
  }
}