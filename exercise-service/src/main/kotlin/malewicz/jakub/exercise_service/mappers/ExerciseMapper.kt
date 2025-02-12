package malewicz.jakub.exercise_service.mappers

import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.ExerciseDetails
import malewicz.jakub.exercise_service.entities.ExerciseEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface ExerciseMapper {
  fun toExerciseBasicResponse(exercise: ExerciseEntity): ExerciseBasicsResponse

  fun toExerciseDetails(exercise: ExerciseEntity): ExerciseDetails
}
