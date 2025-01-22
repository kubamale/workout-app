package malewicz.jakub.workout_service.exercise.mappers

import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ExerciseMapper {
    fun toExerciseDetails(exercise: ExerciseEntity): ExerciseDetails
}