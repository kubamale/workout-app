package malewicz.jakub.statistics_service.exercises.entities

import jakarta.persistence.*
import malewicz.jakub.statistics_service.sets.entities.DistanceSetEntity
import malewicz.jakub.statistics_service.sets.entities.TimeSetEntity
import malewicz.jakub.statistics_service.sets.entities.WeightSetEntity
import malewicz.jakub.statistics_service.workouts.entities.UserWorkoutEntity
import java.util.*

@Entity(name = "exercises")
abstract class ExerciseEntity(
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  var id: UUID? = null,
  var exerciseId: UUID,
  var workoutExerciseId: UUID,
  @ManyToOne(fetch = FetchType.LAZY)
  var workout: UserWorkoutEntity,
)

@Entity(name = "weight")
class WeightExerciseEntity(
  id: UUID? = null,
  exerciseId: UUID,
  workoutExerciseId: UUID,
  workout: UserWorkoutEntity,
  @OneToMany(mappedBy = "exercise", cascade = [CascadeType.ALL], orphanRemoval = true)
  var sets: MutableList<WeightSetEntity> = mutableListOf(),
) : ExerciseEntity(id, exerciseId, workoutExerciseId, workout) {
  constructor(
    exerciseId: UUID,
    workoutExerciseId: UUID,
    workout: UserWorkoutEntity,
    sets: MutableList<WeightSetEntity>
  ) : this(null, exerciseId, workoutExerciseId, workout, sets)
}

@Entity(name = "time")
class TimeExerciseEntity(
  id: UUID? = null,
  exerciseId: UUID,
  workoutExerciseId: UUID,
  workout: UserWorkoutEntity,
  @OneToMany(mappedBy = "exercise", cascade = [CascadeType.ALL], orphanRemoval = true)
  var sets: MutableList<TimeSetEntity> = mutableListOf(),
) : ExerciseEntity(id, exerciseId, workoutExerciseId, workout) {
  constructor(
    exerciseId: UUID,
    workoutExerciseId: UUID,
    workout: UserWorkoutEntity,
    sets: MutableList<TimeSetEntity>
  ) : this(null, exerciseId, workoutExerciseId, workout, sets)
}

@Entity(name = "distance")
class DistanceExerciseEntity(
  id: UUID? = null,
  exerciseId: UUID,
  workoutExerciseId: UUID,
  workout: UserWorkoutEntity,
  @OneToMany(mappedBy = "exercise", cascade = [CascadeType.ALL], orphanRemoval = true)
  var sets: MutableList<DistanceSetEntity> = mutableListOf(),
) : ExerciseEntity(id, exerciseId, workoutExerciseId, workout) {
  constructor(
    exerciseId: UUID,
    workoutExerciseId: UUID,
    workout: UserWorkoutEntity,
    sets: MutableList<DistanceSetEntity>
  ) : this(null, exerciseId, workoutExerciseId, workout, sets)
}


