package malewicz.jakub.statistics_service.measurements.repositories

import malewicz.jakub.statistics_service.measurements.entities.MeasurementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.*

interface MeasurementRepository : JpaRepository<MeasurementEntity, UUID> {
    fun findFirstByUserIdOrderByDateDesc(userId: UUID): MeasurementEntity?
    fun findAllByUserIdAndDateIsGreaterThanEqualOrderByDateDesc(userId: UUID, date: LocalDateTime): List<MeasurementEntity>
}