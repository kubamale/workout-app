package malewicz.jakub.workout_service.workout.entities

import jakarta.persistence.*
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import java.util.*
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity

@Entity(name = "workout_exercises")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class WorkoutExerciseEntity(
  @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID? = null,
  @ManyToOne(fetch = FetchType.LAZY) var workout: WorkoutEntity,
  var exerciseId: UUID,
  var exerciseOrder: Int,
) {
  constructor(
    workout: WorkoutEntity,
    exerciseId: UUID,
    order: Int,
  ) : this(null, workout, exerciseId, order)

}

@Entity
class WeightWorkoutExerciseEntity(
  id: UUID? = null,
  workout: WorkoutEntity,
  exerciseId: UUID,
  order: Int,
  @OneToMany(mappedBy = "workoutExercise", cascade = [CascadeType.ALL], orphanRemoval = true)
  var sets: MutableList<WeightSetEntity> = mutableListOf(),
) : WorkoutExerciseEntity(
  id,
  workout,
  exerciseId,
  order,
) {
  constructor(
    workout: WorkoutEntity,
    exerciseId: UUID,
    order: Int,
    sets: MutableList<WeightSetEntity> = mutableListOf()
  ) : this(null, workout, exerciseId, order, sets)

}

@Entity
class TimeWorkoutExerciseEntity(
  id: UUID? = null,
  workout: WorkoutEntity,
  exerciseId: UUID,
  order: Int,
  @OneToMany(mappedBy = "workoutExercise", cascade = [CascadeType.ALL], orphanRemoval = true)
  var sets: MutableList<TimeSetEntity> = mutableListOf(),
) : WorkoutExerciseEntity(
  id,
  workout,
  exerciseId,
  order,
) {
  constructor(
    workout: WorkoutEntity,
    exerciseId: UUID,
    order: Int,
    sets: MutableList<TimeSetEntity> = mutableListOf()
  ) : this(null, workout, exerciseId, order, sets)
}

@Entity
class DistanceWorkoutExerciseEntity(
  id: UUID? = null,
  workout: WorkoutEntity,
  exerciseId: UUID,
  order: Int,
  @OneToMany(mappedBy = "workoutExercise", cascade = [CascadeType.ALL], orphanRemoval = true)
  var sets: MutableList<DistanceSetEntity> = mutableListOf(),
) : WorkoutExerciseEntity(
  id,
  workout,
  exerciseId,
  order,
) {
  constructor(
    workout: WorkoutEntity,
    exerciseId: UUID,
    order: Int,
    sets: MutableList<DistanceSetEntity> = mutableListOf()
  ) : this(null, workout, exerciseId, order, sets)
}
