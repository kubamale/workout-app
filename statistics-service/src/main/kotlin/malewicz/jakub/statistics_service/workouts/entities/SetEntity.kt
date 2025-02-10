package malewicz.jakub.statistics_service.workouts.entities

import jakarta.persistence.*
import java.util.*

@Entity(name = "sets")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class SetEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    var exerciseStatistic: ExerciseStatisticsEntity? = null,
)

@Entity
class WeightSetEntity(
    id: UUID? = null,
    var reps: Int,
    var weight: Double,
) : SetEntity(id) {
    constructor(reps: Int, weight: Double) : this(
        null,
        reps,
        weight
    )
}

@Entity
class TimeSetEntity(
    id: UUID? = null,
    var time: Long,
    var weight: Double,
) : SetEntity(id) {
    constructor(time: Long, weight: Double) : this(
        null,
        time,
        weight
    )
}

@Entity
class DistanceSetEntity(
    id: UUID? = null,
    var distance: Double,
) : SetEntity(id) {
    constructor(distance: Double) : this(null, distance)
}