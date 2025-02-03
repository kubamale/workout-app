package malewicz.jakub.workout_service.weight

import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.math.RoundingMode

@Component
class KilogramPoundsConverter : WeightConverter {

    private val kilogramInPounds = 2.205

    override fun fromKilograms(kilograms: Double?): Double {
        if (kilograms != null) {
            return BigDecimal.valueOf(kilograms * kilogramInPounds).setScale(2, RoundingMode.HALF_UP).toDouble()
        }
        return 0.0
    }

    override fun toKilograms(value: Double?): Double {
        if (value != null) {
            return BigDecimal.valueOf(value / kilogramInPounds).setScale(2, RoundingMode.HALF_UP).toDouble()
        }
        return 0.0
    }
}