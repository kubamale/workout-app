package malewicz.jakub.statistics_service.measurements.entities

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity(name = "measurements")
class MeasurementEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @Column(nullable = false)
    var userId: UUID,
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    var date: LocalDateTime,
    var weight: Double,
    var bodyFat: Double? = null,
    var leftArm: Double? = null,
    var rightArm: Double? = null,
    var chest: Double? = null,
    var waist: Double? = null,
    var hips: Double? = null,
    var leftThigh: Double? = null,
    var rightThigh: Double? = null,
    var leftCalf: Double? = null,
    var rightCalf: Double? = null,
    var shoulders: Double? = null
)