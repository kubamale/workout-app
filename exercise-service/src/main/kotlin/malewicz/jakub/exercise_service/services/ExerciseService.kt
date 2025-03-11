package malewicz.jakub.exercise_service.services

import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.dtos.PageableResponse
import malewicz.jakub.exercise_service.entities.ExerciseEntity
import malewicz.jakub.exercise_service.exceptions.ResourceNotFoundException
import malewicz.jakub.exercise_service.mappers.ExerciseMapper
import malewicz.jakub.exercise_service.repositories.ExerciseRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class ExerciseService(
  private val exerciseRepository: ExerciseRepository,
  private val exerciseMapper: ExerciseMapper,
  private val exerciseFilterService: FilterService<ExerciseEntity>
) {
  fun getAllExercises(
    pageRequest: PageRequest,
    filters: List<FilterRequest>
  ): PageableResponse<ExerciseBasicsResponse> {
    val specification = exerciseFilterService.getSpecificationFromFilters(filters)
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

  fun getAllByIds(ids: List<UUID>) =
    exerciseRepository
      .findAllByIdIn(ids)
      .map { exerciseMapper.toExerciseDetails(it) }
      .toList()

  fun getBasicExercisesInformation(ids: List<UUID>): List<ExerciseBasicsResponse> =
    exerciseRepository
      .findAllByIdIn(ids)
      .map { exerciseMapper.toExerciseBasicResponse(it) }
      .toList()
}
