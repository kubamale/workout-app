package malewicz.jakub.statistics_service.measurements.dtos

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import java.time.LocalDateTime

data class MeasurementDetails(
    var date: LocalDateTime? = null,
    @field:Min(0)
    var weight: Double,
    @field:Min(0)
    @field:Max(100)
    var bodyFat: Double? = null,
    @field:Min(0)
    var leftArm: Double? = null,
    @field:Min(0)
    var rightArm: Double? = null,
    @field:Min(0)
    var chest: Double? = null,
    @field:Min(0)
    var waist: Double? = null,
    @field:Min(0)
    var hips: Double? = null,
    @field:Min(0)
    var leftThigh: Double? = null,
    @field:Min(0)
    var rightThigh: Double? = null,
    @field:Min(0)
    var leftCalf: Double? = null,
    @field:Min(0)
    var rightCalf: Double? = null,
    @field:Min(0)
    var shoulders: Double? = null
)

