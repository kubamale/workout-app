package malewicz.jakub.statistics_service.workouts.entities

import jakarta.persistence.*
import malewicz.jakub.statistics_service.exercises.entities.ExerciseEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity(name = "user_workouts")
class UserWorkoutEntity(
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  var id: UUID? = null,
  var userId: UUID,
  var workoutId: UUID,
  @Column(columnDefinition = "TIMESTAMP")
  var date: LocalDateTime,
  @OneToMany(mappedBy = "workout", cascade = [CascadeType.ALL])
  var exerciseEntity: MutableList<ExerciseEntity> = mutableListOf(),
)