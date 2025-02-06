package malewicz.jakub.statistics_service.measurements.services

import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.entities.MeasurementEntity
import malewicz.jakub.statistics_service.measurements.mappers.MeasurementMapper
import malewicz.jakub.statistics_service.measurements.repositories.MeasurementRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class MeasurementServiceTest {

    @Mock
    private lateinit var measurementRepository: MeasurementRepository

    @Mock
    private lateinit var measurementMapper: MeasurementMapper

    @InjectMocks
    private lateinit var measurementService: MeasurementService

    @Test
    fun `add measurement should should save a measurement entity`() {
        val userId = UUID.randomUUID()
        val details = MeasurementDetails(
            weight = 100.0,
            bodyFat = 20.0,
            leftArm = 50.0,
            rightArm = 50.0,
            chest = 100.0,
            waist = 100.0,
            hips = 100.0,
            leftThigh = 100.0,
            rightThigh = 100.0,
            leftCalf = 60.0,
            rightCalf = 60.0,
            shoulders = 100.0,
        )

        val measurement = MeasurementEntity(
            id = UUID.randomUUID(),
            userId = userId,
            date = LocalDateTime.now(),
            weight = 100.0,
            bodyFat = 20.0,
            leftArm = 50.0,
            rightArm = 50.0,
            chest = 100.0,
            waist = 100.0,
            hips = 100.0,
            leftThigh = 100.0,
            rightThigh = 100.0,
            leftCalf = 60.0,
            rightCalf = 60.0,
            shoulders = 100.0,
        )

        `when`(measurementMapper.toMeasurementEntity(details, userId)).thenReturn(measurement)
        measurementService.add(details, userId)
        verify(measurementRepository).save(measurement)
    }
}