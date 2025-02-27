package malewicz.jakub.exercise_service.services

import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.entities.*
import malewicz.jakub.exercise_service.exceptions.BadRequestException
import malewicz.jakub.exercise_service.repositories.ExerciseRepository
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class ExerciseFilterService : FilterService<ExerciseEntity> {

  override fun getSpecificationFromFilters(filters: List<FilterRequest>): Specification<ExerciseEntity> {
    return filters.map(this::getSpecification).reduce(Specification<ExerciseEntity>::and).or(null)
  }

  private fun getSpecification(filter: FilterRequest): Specification<ExerciseEntity> {
    try {
      return when (filter.field.lowercase()) {
        ExerciseEntity_.TYPE.lowercase() -> ExerciseRepository.byType(ExerciseType.valueOf(filter.value.toString()))
        ExerciseEntity_.MUSCLE_GROUP.lowercase() -> ExerciseRepository.byMuscleGroup(MuscleGroup.valueOf(filter.value.toString()))
        ExerciseEntity_.EQUIPMENT.lowercase() -> ExerciseRepository.byEquipment(Equipment.valueOf(filter.value.toString()))
        else -> {
          throw BadRequestException("Invalid filter filed provided.")
        }
      }
    } catch (IllegalArgumentException: IllegalArgumentException) {
      throw BadRequestException("Invalid filter value provided.")
    }
  }
}