package malewicz.jakub.statistics_service.exercises.repositories

import malewicz.jakub.statistics_service.exercises.entities.ExerciseEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ExerciseRepository : JpaRepository<ExerciseEntity, UUID> {
  @EntityGraph(attributePaths = ["workout"])
  fun findAllByExerciseIdAndWorkoutUserId(exerciseId: UUID, userId: UUID): List<ExerciseEntity>

  @Query("""
    SELECT DISTINCT e.exerciseId FROM exercises e 
    WHERE e.workout.userId = :userId
  """)
  fun getAllExerciseIdsForUser(userId: UUID): List<UUID>

}