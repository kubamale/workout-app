package malewicz.jakub.exercise_service.services

import java.util.*
import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.dtos.PageableResponse
import malewicz.jakub.exercise_service.exceptions.ResourceNotFoundException
import malewicz.jakub.exercise_service.mappers.ExerciseMapper
import malewicz.jakub.exercise_service.repositories.ExerciseRepository
import malewicz.jakub.exercise_service.repositories.ExerciseSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository,
    private val exerciseMapper: ExerciseMapper
) {
  fun getAllExercises(
      pageRequest: PageRequest,
      filters: List<FilterRequest>
  ): PageableResponse<ExerciseBasicsResponse> {
    val specification = ExerciseSpecification(filters)
    val page = exerciseRepository.findAll(specification, pageRequest)
    return PageableResponse(
        page.totalElements,
        page.number,
        page.size,
        page.hasNext(),
        page.totalPages,
        page.content.map { exerciseMapper.toExerciseBasicResponse(it) })
  }

  fun getDetails(id: UUID) =
      exerciseRepository
          .findById(id)
          .orElseThrow { ResourceNotFoundException("No exercise found with id $id") }
          .let { exerciseMapper.toExerciseDetails(it) }
}
