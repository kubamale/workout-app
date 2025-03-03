package malewicz.jakub.workout_service.workout.mappers

import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.set.entities.SetEntity
import malewicz.jakub.workout_service.set.mappers.SetMapper
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.dtos.WorkoutExerciseDetails
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.factory.Mappers

@Mapper(componentModel = "spring", uses = [SetMapper::class])
abstract class WorkoutMapper {
  private val setMapper: SetMapper = Mappers.getMapper(SetMapper::class.java)
  abstract fun toWorkoutResponse(workout: WorkoutEntity): WorkoutResponse

  @Mapping(target = "id", source = "workout.id")
  @Mapping(target = "name", source = "workout.name")
  @Mapping(target = "exercises", source = "workoutExerciseDetails")
  abstract fun toWorkoutDetailsResponse(
    workout: WorkoutEntity,
    workoutExerciseDetails: MutableList<WorkoutExerciseDetails> = mutableListOf()
  ): WorkoutDetailsResponse


  fun toWorkoutExerciseDetails(
    workoutExercise: WorkoutExerciseEntity,
    exerciseDetails: ExerciseDetails?,
    sets: List<SetEntity>
  ): WorkoutExerciseDetails {
    return WorkoutExerciseDetails(
      workoutExercise.id!!,
      exerciseDetails,
      workoutExercise.exerciseOrder,
      sets.map { setMapper.toSetResponse(it) }.toMutableList()
    )
  }
}
