package malewicz.jakub.exercise_service.services

import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.entities.Equipment
import malewicz.jakub.exercise_service.entities.ExerciseEntity_
import malewicz.jakub.exercise_service.entities.ExerciseType
import malewicz.jakub.exercise_service.entities.MuscleGroup
import malewicz.jakub.exercise_service.exceptions.BadRequestException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExerciseFilterServiceTest {

  val filterService = ExerciseFilterService()

  @Test
  fun `get specification from filters should throw BadRequestException when passwd incorrect field name`() {
    val filterRequest = FilterRequest(ExerciseType.CARDIO, "not_existing_field")
    assertThrows<BadRequestException> { filterService.getSpecificationFromFilters(listOf(filterRequest)) }
  }

  @Test
  fun `get specification from filters should throw BadRequestException when passwd incorrect value`() {
    val filterRequest = FilterRequest("not_existing_type", ExerciseEntity_.TYPE)
    assertThrows<BadRequestException> { filterService.getSpecificationFromFilters(listOf(filterRequest)) }
  }

  @Test
  fun `get specification from filters should return type specification`() {
    val filterRequest = FilterRequest(ExerciseType.CARDIO, ExerciseEntity_.TYPE)
    val spec = filterService.getSpecificationFromFilters(listOf(filterRequest))
    assertThat(spec).isNotNull
  }

  @Test
  fun `get specification from filters should return muscle group specification`() {
    val filterRequest = FilterRequest(MuscleGroup.LATS, ExerciseEntity_.MUSCLE_GROUP.uppercase())
    val spec = filterService.getSpecificationFromFilters(listOf(filterRequest))
    assertThat(spec).isNotNull
  }

  @Test
  fun `get specification from filters should return equipment specification`() {
    val filterRequest = FilterRequest(Equipment.DUMBBELL, ExerciseEntity_.EQUIPMENT.uppercase())
    val spec = filterService.getSpecificationFromFilters(listOf(filterRequest))
    assertThat(spec).isNotNull
  }
}