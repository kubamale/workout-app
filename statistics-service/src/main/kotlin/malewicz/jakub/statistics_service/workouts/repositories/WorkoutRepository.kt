package malewicz.jakub.statistics_service.workouts.repositories

import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.*

interface WorkoutRepository : JpaRepository<UserWorkoutEntity, UUID> {

  @Query("SELECT uw.date FROM user_workouts uw WHERE uw.userId = :userId ORDER BY uw.date DESC LIMIT 1")
  fun getLatestWorkoutDate(userId: UUID): LocalDateTime?

  fun getAllByUserIdAndDateGreaterThan(userId: UUID, date: LocalDateTime): List<UserWorkoutEntity>
}