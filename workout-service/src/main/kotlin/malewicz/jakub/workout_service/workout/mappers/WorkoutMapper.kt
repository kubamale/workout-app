package malewicz.jakub.workout_service.workout.mappers

import malewicz.jakub.workout_service.exercise.mappers.ExerciseMapper
import malewicz.jakub.workout_service.set.mappers.SetMapper
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.dtos.WorkoutExerciseDetails
import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [ExerciseMapper::class, SetMapper::class])
interface WorkoutMapper {
    fun toWorkoutResponse(workout: WorkoutEntity): WorkoutResponse

    @Mapping(target = "id", source = "workout.id")
    @Mapping(target = "name", source = "workout.name")
    @Mapping(target = "exercises", source = "workoutExercises")
    fun toWorkoutDetailsResponse(
        workout: WorkoutEntity,
        workoutExercises: List<WorkoutExerciseEntity>
    ): WorkoutDetailsResponse

    @Mapping(target = "order", source = "exerciseOrder")
    fun toWorkoutExerciseDetails(workoutExercise: WorkoutExerciseEntity): WorkoutExerciseDetails
}