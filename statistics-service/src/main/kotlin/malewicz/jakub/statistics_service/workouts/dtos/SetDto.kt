package malewicz.jakub.statistics_service.workouts.dtos

import jakarta.validation.constraints.Min

data class SetDto(
    @field:Min(0)
    var reps: Int?,
    @field:Min(0)
    var weight: Double?,
    @field:Min(0)
    var time: Long?,
    @field:Min(0)
    var distance: Double?,
    val setType: SetType
)

enum class SetType {
    TIME, WEIGHT, DISTANCE
}