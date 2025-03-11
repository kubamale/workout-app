package malewicz.jakub.statistics_service.sets.repositories

import malewicz.jakub.statistics_service.sets.entities.WeightSetEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface WeightSetRepository : JpaRepository<WeightSetEntity, UUID> {
  fun findAllByExerciseIdIn(exerciseIds: MutableCollection<UUID>): MutableList<WeightSetEntity>
}