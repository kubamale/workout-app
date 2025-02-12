package malewicz.jakub.exercise_service.repositories

import malewicz.jakub.exercise_service.entities.ExerciseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface ExerciseRepository : JpaRepository<ExerciseEntity, UUID>, JpaSpecificationExecutor<ExerciseEntity>