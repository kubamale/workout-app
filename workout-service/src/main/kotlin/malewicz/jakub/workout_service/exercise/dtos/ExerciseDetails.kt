package malewicz.jakub.workout_service.exercise.dtos

import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import java.util.*

data class ExerciseDetails(
    var id: UUID,
    var name: String,
    var muscleGroup: MuscleGroup,
    var description: String,
    var type: ExerciseType,
    var equipment: Equipment,
    var pictureURL: String? = null,
)
