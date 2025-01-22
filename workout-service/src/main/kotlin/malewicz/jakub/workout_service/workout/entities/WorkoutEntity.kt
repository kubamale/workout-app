package malewicz.jakub.workout_service.workout.entities

import jakarta.persistence.*
import java.util.*

@Entity(name = "workouts")
class WorkoutEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name: String,
    var userId: UUID,
    @OneToMany(mappedBy = "workout", cascade = [(CascadeType.ALL)])
    var workoutExercises: MutableList<WorkoutExerciseEntity> = mutableListOf(),
)