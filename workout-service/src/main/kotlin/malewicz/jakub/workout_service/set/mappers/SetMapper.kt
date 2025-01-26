package malewicz.jakub.workout_service.set.mappers

import malewicz.jakub.workout_service.set.dtos.SetResponse
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import org.springframework.stereotype.Component

@Component
class SetMapper {
    fun toSetResponse(set: SetEntity): SetResponse {
        return when (set) {
            is DistanceSetEntity -> SetResponse(set.id!!, set.setOrder, set.distance)
            is TimeSetEntity -> SetResponse(set.id!!, set.setOrder, set.time, set.weight)
            is WeightSetEntity -> SetResponse(set.id!!, set.setOrder, set.reps, set.weight)
            else -> throw RuntimeException("Unknown set type: ${set.javaClass.name}")
        }
    }
}