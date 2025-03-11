package malewicz.jakub.statistics_service.sets.repositories

import malewicz.jakub.statistics_service.sets.entities.DistanceSetEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DistanceSetRepository : JpaRepository<DistanceSetEntity, UUID> {
  fun findAllByExerciseIdIn(exerciseIds: MutableCollection<UUID>): MutableList<DistanceSetEntity>
}