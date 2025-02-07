package malewicz.jakub.statistics_service.measurements.controllers

import jakarta.validation.Valid
import malewicz.jakub.statistics_service.conversion.LengthUnits
import malewicz.jakub.statistics_service.conversion.MeasurementUnitsConverter
import malewicz.jakub.statistics_service.conversion.WeightUnits
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/measurements")
class MeasurementController(
    private val measurementService: MeasurementService,
    private val measurementUnitsConverter: MeasurementUnitsConverter
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun addMeasurement(
        @Valid @RequestBody measurement: MeasurementDetails,
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits,
        @RequestHeader("X-Length-Units") lengthUnits: LengthUnits,
    ) {
        measurementService.add(
            measurementUnitsConverter.convertMeasurementUnits(
                measurement = measurement,
                weightSourceUnits = weightUnits,
                weightDestinationUnits = WeightUnits.KG,
                lengthSourceUnits = lengthUnits,
                lengthDestinationUnits = LengthUnits.CM
            ),
            userId
        )
    }

    @GetMapping("/latest")
    fun getLatest(
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits,
        @RequestHeader("X-Length-Units") lengthUnits: LengthUnits
    ): MeasurementDetails {
        val measurement = measurementService.getLatestMeasurement(userId)
        return measurementUnitsConverter.convertMeasurementUnits(
            measurement = measurement,
            weightSourceUnits = WeightUnits.KG,
            weightDestinationUnits = weightUnits,
            lengthSourceUnits = LengthUnits.CM,
            lengthDestinationUnits = lengthUnits
        )
    }
}