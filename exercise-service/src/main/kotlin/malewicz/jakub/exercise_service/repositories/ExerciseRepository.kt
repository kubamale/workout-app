package malewicz.jakub.exercise_service.repositories

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Root
import malewicz.jakub.exercise_service.entities.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*


interface ExerciseRepository : JpaRepository<ExerciseEntity, UUID>, JpaSpecificationExecutor<ExerciseEntity> {
  fun findAllByIdIn(exercisesIds: Collection<UUID>): List<ExerciseEntity>

  companion object {
    fun byType(type: ExerciseType) =
      Specification { root: Root<ExerciseEntity>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
        builder.equal(
          root.get<ExerciseType>(ExerciseEntity_.TYPE), type
        )
      }

    fun byMuscleGroup(muscleGroup: MuscleGroup) =
      Specification { root: Root<ExerciseEntity>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
        builder.equal(
          root.get<MuscleGroup>(ExerciseEntity_.MUSCLE_GROUP), muscleGroup
        )
      }

    fun byEquipment(equipment: Equipment) =
      Specification { root: Root<ExerciseEntity>, _: CriteriaQuery<*>?, builder: CriteriaBuilder ->
        builder.equal(
          root.get<Equipment>(ExerciseEntity_.EQUIPMENT), equipment
        )
      }
  }
}
