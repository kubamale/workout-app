package malewicz.jakub.statistics_service.conversion

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test


class UnitsConverterKtTest {

    @Test
    fun `convert kilograms to kilograms`() {
        val weight = 10.0
        val result = convertWeight(weight, WeightUnits.KG, WeightUnits.KG)
        assertThat(weight).isEqualTo(result)
    }

    @Test
    fun `convert pounds to pounds`() {
        val weight = 10.0
        val result = convertWeight(weight, WeightUnits.LB, WeightUnits.LB)
        assertThat(result).isEqualTo(weight)
    }

    @Test
    fun `convert kilograms to pounds`() {
        val weight = 10.0
        val result = convertWeight(weight, WeightUnits.KG, WeightUnits.LB)
        assertThat(result).isEqualTo(22.05)
    }

    @Test
    fun `convert pounds to kilograms`() {
        val weight = 22.05
        val result = convertWeight(weight, WeightUnits.LB, WeightUnits.KG)
        assertThat(result).isEqualTo(10.0)
    }

    @Test
    fun `convert null weight should return null`() {
        val result = convertWeight(null, WeightUnits.LB, WeightUnits.KG)
        assertThat(result).isNull()
    }

    @Test
    fun `convert null length should return null`() {
        val result = convertLength(null, LengthUnits.CM, LengthUnits.CM)
        assertThat(result).isNull()
    }

    @Test
    fun `convert centimeters to centimeters`() {
        val value = 10.0
        val result = convertLength(value, LengthUnits.CM, LengthUnits.CM)
        assertThat(result).isEqualTo(value)
    }

    @Test
    fun `convert inches to inches`() {
        val value = 10.0
        val result = convertLength(value, LengthUnits.IN, LengthUnits.IN)
        assertThat(result).isEqualTo(value)
    }

    @Test
    fun `convert centimeters to inches`() {
        val value = 10.0
        val result = convertLength(value, LengthUnits.CM, LengthUnits.IN)
        assertThat(result).isEqualTo(3.94)
    }

    @Test
    fun `convert inches to centimeters`() {
        val value = 10.0
        val result = convertLength(value, LengthUnits.IN, LengthUnits.CM)
        assertThat(result).isEqualTo(25.4)
    }
}