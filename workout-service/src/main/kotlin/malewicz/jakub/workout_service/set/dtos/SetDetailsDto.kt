package malewicz.jakub.workout_service.set.dtos

import java.util.*

data class SetDetailsDto(
    val id: UUID?,
    val order: Int,
    val reps: Int?,
    var weight: Double?,
    val time: Long?,
    val distance: Double?,
    val type: SetType
) {
    constructor(
        id: UUID,
        order: Int,
        reps: Int,
        weight: Double,
    ) : this(id, order, reps, weight, null, null, SetType.WEIGHT)

    constructor(
        id: UUID,
        order: Int,
        time: Long,
        weight: Double,
    ) : this(id, order, null, weight, time, null, SetType.TIME)

    constructor(
        id: UUID,
        order: Int,
        distance: Double,
    ) : this(id, order, null, null, null, distance, SetType.DISTANCE)
}

enum class SetType {
    WEIGHT, TIME, DISTANCE
}