package malewicz.jakub.workout_service.exercise.dtos

import java.util.*
import malewicz.jakub.workout_service.exercise.models.Equipment
import malewicz.jakub.workout_service.exercise.models.ExerciseType
import malewicz.jakub.workout_service.exercise.models.MuscleGroup

data class ExerciseDetails(
    var id: UUID,
    var name: String,
    var muscleGroup: MuscleGroup,
    var description: String,
    var type: ExerciseType,
    var equipment: Equipment,
    var pictureURL: String? = null,
)
