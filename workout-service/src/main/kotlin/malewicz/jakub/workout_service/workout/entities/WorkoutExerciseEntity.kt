package malewicz.jakub.workout_service.workout.entities

import jakarta.persistence.*
import java.util.*
import malewicz.jakub.workout_service.set.entities.SetEntity

@Entity(name = "workout_exercises")
class WorkoutExerciseEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID) var id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY) var workout: WorkoutEntity,
    var exerciseId: UUID,
    @OneToMany(mappedBy = "workoutExercise", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sets: MutableList<SetEntity> = mutableListOf(),
    var exerciseOrder: Int
) {
  constructor(
      workout: WorkoutEntity,
      exerciseId: UUID,
      sets: MutableList<SetEntity>,
      order: Int
  ) : this(null, workout, exerciseId, sets, order)
}
