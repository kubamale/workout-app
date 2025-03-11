package malewicz.jakub.statistics_service.exercises.dtos

import java.util.*

data class ExerciseBasicInfo(
    val id: UUID,
    val name: String,
    val pictureURL: String?
)
