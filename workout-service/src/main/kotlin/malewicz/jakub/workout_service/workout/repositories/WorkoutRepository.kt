package malewicz.jakub.workout_service.workout.repositories

import malewicz.jakub.workout_service.workout.entities.WorkoutEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import kotlin.collections.List

interface WorkoutRepository : JpaRepository<WorkoutEntity, UUID> {
    @EntityGraph(attributePaths = ["workoutExercises"])
    fun findByIdAndUserId(id: UUID, userId: UUID): Optional<WorkoutEntity>
    fun findByUserId(userId: UUID): List<WorkoutEntity>
    fun findAllByIdIn(id: Collection<UUID>): List<WorkoutEntity>
}