package malewicz.jakub.workout_service.set.entities

import jakarta.persistence.*
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import java.util.*

@Entity(name = "sets")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class SetEntity(
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  var id: UUID? = null,
  var setOrder: Int,
  @ManyToOne
  var workoutExercise: WorkoutExerciseEntity? = null,
)

@Entity(name = "weight_sets")
class WeightSetEntity(
  id: UUID? = null,
  setOrder: Int,
  workoutExercise: WorkoutExerciseEntity? = null,
  var reps: Int,
  var weight: Double,
) : SetEntity(id, setOrder, workoutExercise) {
  constructor(setOrder: Int, reps: Int, weight: Double) : this(null, setOrder, null, reps, weight)
}

@Entity(name = "time_sets")
class TimeSetEntity(
  id: UUID? = null,
  setOrder: Int,
  workoutExercise: WorkoutExerciseEntity? = null,
  var time: Long,
  var weight: Double,
) : SetEntity(id, setOrder, workoutExercise) {
  constructor(setOrder: Int, time: Long, weight: Double) : this(null, setOrder, null, time, weight)
}

@Entity(name = "distance_sets")
class DistanceSetEntity(
  id: UUID? = null,
  setOrder: Int,
  workoutExercise: WorkoutExerciseEntity? = null,
  var distance: Double,
) : SetEntity(id, setOrder, workoutExercise) {
  constructor(setOrder: Int, distance: Double) : this(null, setOrder, null, distance)
}