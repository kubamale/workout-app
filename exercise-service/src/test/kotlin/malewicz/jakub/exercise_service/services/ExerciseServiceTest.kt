package malewicz.jakub.exercise_service.services

import java.util.*
import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.ExerciseDetails
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.entities.Equipment
import malewicz.jakub.exercise_service.entities.ExerciseEntity
import malewicz.jakub.exercise_service.entities.ExerciseType
import malewicz.jakub.exercise_service.entities.MuscleGroup
import malewicz.jakub.exercise_service.exceptions.ResourceNotFoundException
import malewicz.jakub.exercise_service.mappers.ExerciseMapper
import malewicz.jakub.exercise_service.repositories.ExerciseRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

@ExtendWith(MockitoExtension::class)
class ExerciseServiceTest {

  @Mock
  private lateinit var exerciseRepository: ExerciseRepository

  @Mock
  private lateinit var exerciseMapper: ExerciseMapper

  @Mock
  private lateinit var exerciseFilterService: FilterService<ExerciseEntity>

  @InjectMocks
  private lateinit var exerciseService: ExerciseService

  @Test
  fun `get all exercises should return pageable response`() {
    val page = PageRequest.of(0, 10)
    val filter = FilterRequest(ExerciseType.STRENGTH, "type")
    val exercise =
      ExerciseEntity(
        UUID.randomUUID(),
        "curls",
        MuscleGroup.BICEPS,
        "description",
        ExerciseType.STRENGTH,
        Equipment.DUMBBELL,
      )
    `when`(exerciseFilterService.getSpecificationFromFilters(listOf(filter))).thenReturn(
      ExerciseRepository.byType(
        ExerciseType.STRENGTH
      )
    )
    `when`(exerciseRepository.findAll(any(), ArgumentMatchers.eq(page)))
      .thenReturn(PageImpl(listOf(exercise), page, 5))
    `when`(exerciseMapper.toExerciseBasicResponse(exercise))
      .thenReturn(ExerciseBasicsResponse(exercise.id!!, exercise.name, exercise.pictureURL))
    val result = exerciseService.getAllExercises(page, listOf(filter))
    assertThat(result.results).hasSize(1)
    assertThat(result.totalElements).isEqualTo(1)
    assertThat(result.totalPages).isEqualTo(1)
    assertThat(result.hasNextPage).isFalse()
  }

  @Test
  fun `get details should throw ResourceNotFoundException when no exercise was found`() {
    val id = UUID.randomUUID()
    `when`(exerciseRepository.findById(id)).thenReturn(Optional.empty())
    assertThrows<ResourceNotFoundException> { exerciseService.getDetails(id) }
  }

  @Test
  fun `get details should throw return exercise details`() {
    val id = UUID.randomUUID()
    val exercise =
      ExerciseEntity(
        UUID.randomUUID(),
        "curls",
        MuscleGroup.BICEPS,
        "description",
        ExerciseType.STRENGTH,
        Equipment.DUMBBELL,
      )
    val exerciseDetails =
      ExerciseDetails(
        id,
        "Legs",
        MuscleGroup.LATS,
        "desc",
        ExerciseType.STRETCHING,
        Equipment.NONE,
      )
    `when`(exerciseRepository.findById(id)).thenReturn(Optional.of(exercise))
    `when`(exerciseMapper.toExerciseDetails(exercise)).thenReturn(exerciseDetails)
    val result = exerciseService.getDetails(id)
    assertThat(result.id).isEqualTo(exerciseDetails.id)
  }

  @Test
  fun `get all exercises by Id should return list of exercises`() {
    val exercise1 =
      ExerciseEntity(
        UUID.randomUUID(),
        "curls",
        MuscleGroup.BICEPS,
        "description",
        ExerciseType.STRENGTH,
        Equipment.DUMBBELL,
      )
    val exercise2 =
      ExerciseEntity(
        UUID.randomUUID(),
        "curls",
        MuscleGroup.BICEPS,
        "description",
        ExerciseType.STRENGTH,
        Equipment.DUMBBELL,
      )
    val exerciseDetails =
      ExerciseDetails(
        exercise1.id!!,
        "Legs",
        MuscleGroup.LATS,
        "desc",
        ExerciseType.STRETCHING,
        Equipment.NONE,
      )
    val ids = listOf(exercise1.id!!, exercise2.id!!)
    `when`(exerciseRepository.findAllByIdIn(ids)).thenReturn(listOf(exercise1, exercise2))
    `when`(exerciseMapper.toExerciseDetails(exercise1)).thenReturn(exerciseDetails)
    `when`(exerciseMapper.toExerciseDetails(exercise2)).thenReturn(exerciseDetails)
    val result = exerciseService.getAllByIds(ids)
    assertThat(result.size).isEqualTo(2)
  }
}
