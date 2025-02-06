package malewicz.jakub.statistics_service.measurements.services

import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.mappers.MeasurementMapper
import malewicz.jakub.statistics_service.measurements.repositories.MeasurementRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val measurementMapper: MeasurementMapper
) {

    fun add(measurementDetails: MeasurementDetails, userId: UUID) {
        val measurement = measurementMapper.toMeasurementEntity(measurementDetails, userId)
        measurementRepository.save(measurement)
    }
}