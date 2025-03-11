package malewicz.jakub.statistics_service.sets.repositories

import malewicz.jakub.statistics_service.sets.entities.TimeSetEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface TimeSetRepository : JpaRepository<TimeSetEntity, UUID> {
  fun findAllByExerciseIdIn(exerciseIds: MutableCollection<UUID>): MutableList<TimeSetEntity>
}