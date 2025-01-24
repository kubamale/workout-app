package malewicz.jakub.workout_service.exercise.repositories

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import malewicz.jakub.workout_service.dtos.FilterRequest
import malewicz.jakub.workout_service.exceptions.BadRequestException
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import org.springframework.data.jpa.domain.Specification

class ExerciseSpecification(private val filters: List<FilterRequest>) : Specification<ExerciseEntity> {
    override fun toPredicate(
        root: Root<ExerciseEntity>,
        query: CriteriaQuery<*>?,
        criteriaBuilder: CriteriaBuilder
    ): Predicate? {
        val predicates = filters.map { filter ->
            when (filter.field) {
                "type" -> handleTypeFilter(criteriaBuilder, root, filter)
                "muscleGroup" -> handleMuscleGroupFilter(criteriaBuilder, root, filter)
                "equipment" -> handleEquipmentFilter(criteriaBuilder, root, filter)
                else -> throw BadRequestException("Exercise type ${filter.field} not supported")
            }
        }
        return criteriaBuilder.and(*predicates.toTypedArray())
    }

    private fun handleTypeFilter(
        criteriaBuilder: CriteriaBuilder,
        root: Root<ExerciseEntity>,
        filter: FilterRequest
    ): Predicate {
        try {
            val exerciseType = ExerciseType.valueOf(filter.value.toString().uppercase())
            return criteriaBuilder.equal(root.get<ExerciseType>(filter.field), exerciseType)
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException("Unknown exercise type: ${filter.value}")
        }
    }

    private fun handleMuscleGroupFilter(
        criteriaBuilder: CriteriaBuilder,
        root: Root<ExerciseEntity>,
        filter: FilterRequest
    ): Predicate {
        try {
            val muscleGroup = MuscleGroup.valueOf(filter.value.toString().uppercase())
            return criteriaBuilder.equal(root.get<MuscleGroup>(filter.field), muscleGroup)
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException("Unknown muscle group: ${filter.value}")
        }
    }

    private fun handleEquipmentFilter(
        criteriaBuilder: CriteriaBuilder,
        root: Root<ExerciseEntity>,
        filter: FilterRequest
    ): Predicate {
        try {
            val equipment = Equipment.valueOf(filter.value.toString().uppercase())
            return criteriaBuilder.equal(root.get<Equipment>(filter.field), equipment)
        } catch (ex: IllegalArgumentException) {
            throw BadRequestException("Unknown equipment: ${filter.value}")
        }
    }
}