package malewicz.jakub.statistics_service.measurements.repositories

import malewicz.jakub.statistics_service.measurements.entities.MeasurementEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MeasurementRepository : JpaRepository<MeasurementEntity, UUID>