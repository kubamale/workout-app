package malewicz.jakub.workout_service.exercise.entities

import jakarta.persistence.*
import java.util.*

@Entity(name = "exercises")
class ExerciseEntity(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var name: String,
    @Enumerated(EnumType.STRING)
    var muscleGroup: MuscleGroup,
    var description: String,
    @Enumerated(EnumType.STRING)
    var type: ExerciseType,
    @Enumerated(EnumType.STRING)
    var equipment: Equipment,
    var pictureURL: String? = null,
)

enum class MuscleGroup {
    ABDOMINALS,
    ADDUCTORS,
    ABDUCTORS,
    BICEPS,
    CALVES,
    CHEST,
    FOREARMS,
    GLUTES,
    HAMSTRINGS,
    LATS,
    LOWER_BACK,
    MIDDLE_BACK,
    TRAPS,
    NECK,
    QUADRICEPS,
    SHOULDERS,
    TRICEPS
}

enum class ExerciseType {
    STRENGTH,
    PLYOMETRICS,
    CARDIO,
    STRETCHING,
    POWERLIFTING,
    STRONGMAN,
    OLYMPIC_WEIGHTLIFTING
}

enum class Equipment {
    BANDS,
    BARBELL,
    KETTLEBELLS,
    DUMBBELL,
    OTHER,
    CABLE,
    MACHINE,
    BODY_ONLY,
    MEDICINE_BALL,
    NONE,
    EXERCISE_BALL,
    FOAM_ROLL,
    EZ_CURL_BAR
}