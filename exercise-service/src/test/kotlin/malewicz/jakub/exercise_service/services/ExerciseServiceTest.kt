package malewicz.jakub.exercise_service.services

import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.entities.Equipment
import malewicz.jakub.exercise_service.entities.ExerciseEntity
import malewicz.jakub.exercise_service.entities.ExerciseType
import malewicz.jakub.exercise_service.entities.MuscleGroup
import malewicz.jakub.exercise_service.mappers.ExerciseMapper
import malewicz.jakub.exercise_service.repositories.ExerciseRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockitoExtension::class)
class ExerciseServiceTest {

    @Mock
    private lateinit var exerciseRepository: ExerciseRepository

    @Mock
    private lateinit var exerciseMapper: ExerciseMapper

    @InjectMocks
    private lateinit var exerciseService: ExerciseService

    @Test
    fun `get all exercises should return pageable response`() {
        val page = PageRequest.of(0, 10)
        val filter = FilterRequest(ExerciseType.STRENGTH, "type")
        val exercise = ExerciseEntity(
            UUID.randomUUID(),
            "curls",
            MuscleGroup.BICEPS,
            "description",
            ExerciseType.STRENGTH,
            Equipment.DUMBBELL
        )
        `when`(exerciseRepository.findAll(any(), ArgumentMatchers.eq(page))).thenReturn(
            PageImpl(
                listOf(exercise),
                page,
                5
            )
        )
        `when`(exerciseMapper.toExerciseBasicResponse(exercise)).thenReturn(
            ExerciseBasicsResponse(
                exercise.id!!,
                exercise.name,
                exercise.pictureURL
            )
        )
        val result = exerciseService.getAllExercises(page, listOf(filter))
        assertThat(result.results).hasSize(1)
        assertThat(result.totalElements).isEqualTo(1)
        assertThat(result.totalPages).isEqualTo(1)
        assertThat(result.hasNextPage).isFalse()
    }
}