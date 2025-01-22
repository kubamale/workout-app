package malewicz.jakub.workout_service.exercise.repositories

import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ExerciseRepository : JpaRepository<ExerciseEntity, UUID>