package malewicz.jakub.workout_service.set.mappers

import malewicz.jakub.workout_service.set.dtos.SetType
import malewicz.jakub.workout_service.set.entities.DistanceSetEntity
import malewicz.jakub.workout_service.set.entities.TimeSetEntity
import malewicz.jakub.workout_service.set.entities.WeightSetEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class SetMapperTest {

    private val mapper = SetMapper()

    @Test
    fun `to Set Response should return Set Response with Weight Type`() {
        val set = WeightSetEntity(UUID.randomUUID(), 0, null, 10, 20.0)
        val setResponse = mapper.toSetResponse(set)
        assertThat(setResponse.id).isEqualTo(set.id)
        assertThat(setResponse.order).isEqualTo(set.setOrder)
        assertThat(setResponse.weight).isEqualTo(set.weight)
        assertThat(setResponse.reps).isEqualTo(set.reps)
        assertThat(setResponse.time).isNull()
        assertThat(setResponse.distance).isNull()
        assertThat(setResponse.type).isEqualTo(SetType.WEIGHT)
    }

    @Test
    fun `to Set Response should return Set Response with Time Type`() {
        val set = TimeSetEntity(UUID.randomUUID(), 0, null, 10, 20.0)
        val setResponse = mapper.toSetResponse(set)
        assertThat(setResponse.id).isEqualTo(set.id)
        assertThat(setResponse.order).isEqualTo(set.setOrder)
        assertThat(setResponse.weight).isEqualTo(set.weight)
        assertThat(setResponse.reps).isNull()
        assertThat(setResponse.time).isEqualTo(set.time)
        assertThat(setResponse.distance).isNull()
        assertThat(setResponse.type).isEqualTo(SetType.TIME)
    }

    @Test
    fun `to Set Response should return Set Response with Distance Type`() {
        val set = DistanceSetEntity(UUID.randomUUID(), 0, null, 10.0)
        val setResponse = mapper.toSetResponse(set)
        assertThat(setResponse.id).isEqualTo(set.id)
        assertThat(setResponse.order).isEqualTo(set.setOrder)
        assertThat(setResponse.weight).isNull()
        assertThat(setResponse.reps).isNull()
        assertThat(setResponse.time).isNull()
        assertThat(setResponse.distance).isEqualTo(set.distance)
        assertThat(setResponse.type).isEqualTo(SetType.DISTANCE)
    }
}