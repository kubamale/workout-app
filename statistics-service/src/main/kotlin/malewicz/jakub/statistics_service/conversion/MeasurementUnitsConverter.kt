package malewicz.jakub.statistics_service.conversion

import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import org.springframework.stereotype.Component

@Component
class MeasurementUnitsConverter {
    fun convertMeasurementUnits(
        measurement: MeasurementDetails,
        weightSourceUnits: WeightUnits,
        weightDestinationUnits: WeightUnits,
        lengthSourceUnits: LengthUnits,
        lengthDestinationUnits: LengthUnits,
    ): MeasurementDetails {
        measurement.weight = convertWeight(measurement.weight, weightSourceUnits, weightDestinationUnits)!!
        measurement.leftArm = convertLength(measurement.leftArm, lengthSourceUnits, lengthDestinationUnits)
        measurement.rightArm = convertLength(measurement.rightArm, lengthSourceUnits, lengthDestinationUnits)
        measurement.chest = convertLength(measurement.chest, lengthSourceUnits, lengthDestinationUnits)
        measurement.waist = convertLength(measurement.waist, lengthSourceUnits, lengthDestinationUnits)
        measurement.hips = convertLength(measurement.hips, lengthSourceUnits, lengthDestinationUnits)
        measurement.leftThigh = convertLength(measurement.leftThigh, lengthSourceUnits, lengthDestinationUnits)
        measurement.rightThigh = convertLength(measurement.rightThigh, lengthSourceUnits, lengthDestinationUnits)
        measurement.shoulders = convertLength(measurement.shoulders, lengthSourceUnits, lengthDestinationUnits)
        measurement.leftCalf = convertLength(measurement.leftCalf, lengthSourceUnits, lengthDestinationUnits)
        measurement.rightCalf = convertLength(measurement.rightCalf, lengthSourceUnits, lengthDestinationUnits)
        return measurement
    }
}