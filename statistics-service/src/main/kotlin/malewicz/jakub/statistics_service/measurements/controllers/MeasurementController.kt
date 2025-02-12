package malewicz.jakub.statistics_service.measurements.controllers

import jakarta.validation.Valid
import java.time.LocalDateTime
import java.util.*
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/measurements")
class MeasurementController(private val measurementService: MeasurementService) {

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  fun addMeasurement(
      @Valid @RequestBody measurement: MeasurementDetails,
      @RequestHeader("X-User-Id") userId: UUID
  ) {
    measurementService.add(measurement, userId)
  }

  @GetMapping("/latest")
  fun getLatest(@RequestHeader("X-User-Id") userId: UUID) =
      measurementService.getLatestMeasurement(userId)

  @GetMapping
  fun getMeasurementsSince(
      @RequestParam from: LocalDateTime,
      @RequestHeader("X-User-Id") userId: UUID
  ) = measurementService.getMeasurementsSince(userId, from)
}
