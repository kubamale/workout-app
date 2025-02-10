package malewicz.jakub.statistics_service.conversion

import malewicz.jakub.statistics_service.workouts.dtos.SetDto
import org.springframework.stereotype.Component

@Component
class SetUnitsConverter {
    fun convertSetUnits(
        set: SetDto,
        weightSourceUnits: WeightUnits,
        weightDestinationUnits: WeightUnits,
        lengthSourceUnits: LengthUnits,
        lengthDestinationUnits: LengthUnits,
    ): SetDto {
        set.weight = convertWeight(set.weight, weightSourceUnits, weightDestinationUnits)
        set.distance = convertDistance(set.distance, lengthSourceUnits, lengthDestinationUnits)
        return set
    }
}