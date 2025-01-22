package malewicz.jakub.workout_service.set.entities

import jakarta.persistence.*
import malewicz.jakub.workout_service.workout.entities.WorkoutExerciseEntity
import java.util.*

@Entity(name = "sets")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class SetEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var setOrder: Int,
    @ManyToOne
    var workoutExercise: WorkoutExerciseEntity,
)

@Entity
class WeightSetEntity(
    id: UUID? = null,
    setOrder: Int,
    workoutExercise: WorkoutExerciseEntity,
    var reps: Int,
    var weight: Double,

    ) : SetEntity(id, setOrder, workoutExercise)

@Entity
class TimeSetEntity(
    id: UUID? = null,
    setOrder: Int,
    workoutExercise: WorkoutExerciseEntity,
    var time: Long,
    var weight: Double? = null,
) : SetEntity(id, setOrder, workoutExercise)

@Entity
class DistanceSetEntity(
    id: UUID? = null,
    setOrder: Int,
    workoutExercise: WorkoutExerciseEntity,
    var distance: Double,
) : SetEntity(id, setOrder, workoutExercise)