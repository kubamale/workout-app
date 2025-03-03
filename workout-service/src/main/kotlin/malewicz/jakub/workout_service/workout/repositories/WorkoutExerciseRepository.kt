package malewicz.jakub.workout_service.workout.repositories

import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WorkoutExerciseRepository : JpaRepository<WorkoutExerciseEntity, UUID> {
  fun findByIdAndWorkoutId(id: UUID, workoutId: UUID): Optional<WorkoutExerciseEntity>
}
