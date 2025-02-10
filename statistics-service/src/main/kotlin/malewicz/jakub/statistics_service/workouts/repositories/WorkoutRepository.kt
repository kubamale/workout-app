package malewicz.jakub.statistics_service.workouts.repositories

import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface WorkoutRepository : JpaRepository<UserWorkoutEntity, UUID> {
}