package malewicz.jakub.workout_service.workout.entities

import jakarta.persistence.*
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import java.util.*

@Entity(name = "workout_exercises")
class WorkoutExerciseEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    var workout: WorkoutEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    var exercise: ExerciseEntity,
    @OneToMany(mappedBy = "workoutExercise", cascade = [CascadeType.ALL], orphanRemoval = true)
    var sets: MutableList<SetEntity> = mutableListOf(),
    var exerciseOrder: Int
) {
    constructor(workout: WorkoutEntity, exercise: ExerciseEntity, sets: MutableList<SetEntity>, order: Int) : this(
        null,
        workout,
        exercise,
        sets,
        order
    )
}