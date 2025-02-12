package malewicz.jakub.workout_service.workout.mappers

import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.set.mappers.SetMapper
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.dtos.WorkoutExerciseDetails
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [SetMapper::class])
interface WorkoutMapper {
  fun toWorkoutResponse(workout: WorkoutEntity): WorkoutResponse

  @Mapping(target = "id", source = "workout.id")
  @Mapping(target = "name", source = "workout.name")
  @Mapping(target = "exercises", source = "workoutExerciseDetails")
  fun toWorkoutDetailsResponse(
      workout: WorkoutEntity,
      workoutExerciseDetails: MutableList<WorkoutExerciseDetails> = mutableListOf()
  ): WorkoutDetailsResponse

  @Mapping(target = "exercise", ignore = true)
  @Mapping(target = "order", source = "exerciseOrder")
  fun toWorkoutExerciseDetails(
      workoutExercise: WorkoutExerciseEntity,
      exerciseDetails: ExerciseDetails
  ): WorkoutExerciseDetails
}
