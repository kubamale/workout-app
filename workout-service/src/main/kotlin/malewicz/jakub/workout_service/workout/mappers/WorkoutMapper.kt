package malewicz.jakub.workout_service.workout.mappers

import malewicz.jakub.workout_service.workout.dtos.WorkoutResponse
import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface WorkoutMapper {
    fun toWorkoutResponse(workout: WorkoutEntity): WorkoutResponse
}