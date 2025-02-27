package malewicz.jakub.workout_service.workout.repositories

import java.util.*
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutExerciseRepository : JpaRepository<WorkoutExerciseEntity, UUID> {

  @EntityGraph(attributePaths = ["sets"])
  fun findByWorkoutId(workoutId: UUID): List<WorkoutExerciseEntity>

  fun findByIdAndWorkoutId(id: UUID, workoutId: UUID): Optional<WorkoutExerciseEntity>
}
