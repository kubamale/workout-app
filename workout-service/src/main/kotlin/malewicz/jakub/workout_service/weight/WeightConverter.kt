package malewicz.jakub.workout_service.weight

interface WeightConverter {
    fun fromKilograms(kilograms: Double?): Double
    fun toKilograms(value: Double?): Double
}