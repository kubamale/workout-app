package malewicz.jakub.statistics_service.workouts.entities

import jakarta.persistence.*
import malewicz.jakub.statistics_service.workouts.mappers.Default
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class SetEntity(
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  var id: UUID? = null,
  @ManyToOne(fetch = FetchType.LAZY)
  var exercise: ExerciseEntity? = null,
)

@Entity(name = "weight_sets")
class WeightSetEntity(
  id: UUID? = null,
  var reps: Int,
  var weight: Double,
) : SetEntity(id) {
  @Default
  constructor(reps: Int, weight: Double) : this(
    null,
    reps,
    weight
  )
}

@Entity(name = "time_sets")
class TimeSetEntity(
  id: UUID? = null,
  var time: Long,
  var weight: Double,
) : SetEntity(id) {
  @Default
  constructor(time: Long, weight: Double) : this(
    null,
    time,
    weight
  )
}

@Entity(name = "distance_sets")
class DistanceSetEntity(
  id: UUID? = null,
  var distance: Double,
) : SetEntity(id) {
  @Default
  constructor(distance: Double) : this(null, distance)
}