package malewicz.jakub.workout_service.weight

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KilogramPoundsConverterTest {

    private val converter = KilogramPoundsConverter()

    @Test
    fun `from Kilograms`() {
        assertThat(converter.fromKilograms(10.0)).isEqualTo(22.05)
        assertThat(converter.fromKilograms(null)).isEqualTo(0.0)
    }

    @Test
    fun `to Kilograms`() {
        assertThat(converter.toKilograms(22.05)).isEqualTo(10.0)
        assertThat(converter.toKilograms(null)).isEqualTo(0.0)
    }
}