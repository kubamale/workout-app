package malewicz.jakub.workout_service.set.mappers

import malewicz.jakub.workout_service.set.dtos.SetDetailsDto
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.SetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class SetMapper {
    fun toSetResponse(set: SetEntity): SetDetailsDto {
        return when (set) {
            is DistanceSetEntity -> SetDetailsDto(set.id!!, set.setOrder, set.distance)
            is TimeSetEntity -> SetDetailsDto(set.id!!, set.setOrder, set.time, set.weight)
            is WeightSetEntity -> SetDetailsDto(set.id!!, set.setOrder, set.reps, set.weight)
            else -> throw RuntimeException("Unknown set type: ${set.javaClass.name}")
        }
    }
}