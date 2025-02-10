package malewicz.jakub.statistics_service.conversion

import java.math.BigDecimal
import java.math.RoundingMode


private const val kilogramInPounds = 2.205
private const val inchToCentimeters = 2.54
private const val mileInKilometers = 1.609


fun convertWeight(value: Double?, sourceUnits: WeightUnits, destinationUnits: WeightUnits): Double? {
    if (value == null) return null
    if (sourceUnits == destinationUnits) return value

    val valueInKilograms = when (sourceUnits) {
        WeightUnits.KG -> value
        WeightUnits.LB -> BigDecimal.valueOf(value / kilogramInPounds).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    return when (destinationUnits) {
        WeightUnits.KG -> valueInKilograms
        WeightUnits.LB -> BigDecimal.valueOf(valueInKilograms * kilogramInPounds).setScale(2, RoundingMode.HALF_UP)
            .toDouble()
    }
}

fun convertLength(value: Double?, sourceUnits: LengthUnits, destinationUnits: LengthUnits): Double? {
    if (value == null) return null
    if (sourceUnits == destinationUnits) return value

    val valueInCentimeters = when (sourceUnits) {
        LengthUnits.CM -> value
        LengthUnits.IN -> BigDecimal.valueOf(value * inchToCentimeters).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    return when (destinationUnits) {
        LengthUnits.CM -> valueInCentimeters
        LengthUnits.IN -> BigDecimal.valueOf(valueInCentimeters / inchToCentimeters)
            .setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}

fun convertDistance(value: Double?, sourceUnits: LengthUnits, destinationUnits: LengthUnits): Double? {
    if (value == null) return null
    if (sourceUnits == destinationUnits) return value

    val valueInKilometers = when (sourceUnits) {
        LengthUnits.CM -> value
        LengthUnits.IN -> BigDecimal.valueOf(value * mileInKilometers).setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    return when (destinationUnits) {
        LengthUnits.CM -> valueInKilometers
        LengthUnits.IN -> BigDecimal.valueOf(valueInKilometers / mileInKilometers)
            .setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}

