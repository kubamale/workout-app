package malewicz.jakub.statistics_service.workouts.entities

import jakarta.persistence.*
import java.util.*

@Entity
class ExerciseStatisticsEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var exerciseId: UUID,
    var workoutExerciseId: UUID,
    @ManyToOne(fetch = FetchType.LAZY)
    var workout: UserWorkoutEntity,
    @OneToMany(mappedBy = "exerciseStatistic", cascade = [CascadeType.ALL])
    var sets: MutableList<SetEntity> = mutableListOf(),
)