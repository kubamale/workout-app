package malewicz.jakub.exercise_service.dtos

import java.util.*
import malewicz.jakub.exercise_service.entities.Equipment
import malewicz.jakub.exercise_service.entities.ExerciseType
import malewicz.jakub.exercise_service.entities.MuscleGroup

data class ExerciseDetails(
    var id: UUID,
    var name: String,
    var muscleGroup: MuscleGroup,
    var description: String,
    var type: ExerciseType,
    var equipment: Equipment,
    var pictureURL: String? = null,
)
