package malewicz.jakub.workout_service.set.repositories

import malewicz.jakub.workout_service.set.entities.SetEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface SetRepository : JpaRepository<SetEntity, UUID> {
    fun findAllByWorkoutExerciseId(workoutExercisesId: UUID): List<SetEntity>
}
