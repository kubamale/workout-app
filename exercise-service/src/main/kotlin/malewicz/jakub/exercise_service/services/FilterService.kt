package malewicz.jakub.exercise_service.services

import malewicz.jakub.exercise_service.dtos.FilterRequest
import org.springframework.data.jpa.domain.Specification

interface FilterService<T> {
  fun getSpecificationFromFilters(filters: List<FilterRequest>): Specification<T>
}