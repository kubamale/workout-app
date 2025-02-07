package malewicz.jakub.statistics_service.measurements.services

import malewicz.jakub.statistics_service.exceptions.ResourceNotFoundException
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

    fun getLatestMeasurement(userId: UUID): MeasurementDetails {
        val measurement = measurementRepository.findFirstByUserIdOrderByDateDesc(userId)
            ?: throw ResourceNotFoundException("You have no measurements yet.")
        return measurementMapper.toMeasurementDetails(measurement)
    }
}