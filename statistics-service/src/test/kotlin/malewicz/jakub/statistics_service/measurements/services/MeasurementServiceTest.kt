package malewicz.jakub.statistics_service.measurements.services

import malewicz.jakub.statistics_service.exceptions.ResourceNotFoundException
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.entities.MeasurementEntity
import malewicz.jakub.statistics_service.measurements.mappers.MeasurementMapper
import malewicz.jakub.statistics_service.measurements.repositories.MeasurementRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

    private val details = MeasurementDetails(
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

    private val measurement = MeasurementEntity(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
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

    @Test
    fun `add measurement should should save a measurement entity`() {
        `when`(measurementMapper.toMeasurementEntity(details, measurement.userId)).thenReturn(measurement)
        measurementService.add(details, measurement.userId)
        verify(measurementRepository).save(measurement)
    }

    @Test
    fun `get latest measurement should throw ResourceNotFoundException when no measurement were saved for user`() {
        val userId = UUID.randomUUID()
        `when`(measurementRepository.findFirstByUserIdOrderByDateDesc(userId)).thenReturn(null)
        assertThrows<ResourceNotFoundException> { measurementService.getLatestMeasurement(userId) }
    }

    @Test
    fun `get latest measurement should return measurement details`() {
        `when`(measurementRepository.findFirstByUserIdOrderByDateDesc(measurement.userId)).thenReturn(measurement)
        `when`(measurementMapper.toMeasurementDetails(measurement)).thenReturn(details)
        val result = measurementService.getLatestMeasurement(measurement.userId)
        assertEquals(details, result)
    }

    @Test
    fun `get measurements since returns measurements`() {
        val userId = UUID.randomUUID()
        val fromDate = LocalDateTime.now()
        `when`(
            measurementRepository.findAllByUserIdAndDateIsGreaterThanEqualOrderByDateDesc(
                userId,
                fromDate
            )
        ).thenReturn(
            mutableListOf(measurement)
        )

        `when`(measurementMapper.toMeasurementDetails(measurement)).thenReturn(details)
        val result = measurementService.getMeasurementsSince(userId, fromDate)
        assertThat(result).hasSize(1)
        assertThat(result[0]).isEqualTo(details)
    }
}