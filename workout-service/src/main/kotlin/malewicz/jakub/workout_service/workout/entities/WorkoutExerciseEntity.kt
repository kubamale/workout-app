package malewicz.jakub.workout_service.workout.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import java.util.*

@Entity(name = "workout_exercises")
class WorkoutExerciseEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    @ManyToOne
    var workout: WorkoutEntity,
    @ManyToOne
    var exercise: ExerciseEntity,
    @OneToMany(mappedBy = "workoutExercise")
    var sets: MutableList<SetEntity> = mutableListOf(),
)