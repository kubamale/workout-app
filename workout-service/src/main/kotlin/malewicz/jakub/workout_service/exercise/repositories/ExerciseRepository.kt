package malewicz.jakub.workout_service.exercise.repositories

import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface ExerciseRepository : JpaRepository<ExerciseEntity, UUID>, JpaSpecificationExecutor<ExerciseEntity>