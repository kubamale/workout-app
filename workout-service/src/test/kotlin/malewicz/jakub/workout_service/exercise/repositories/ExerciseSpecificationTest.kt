package malewicz.jakub.workout_service.exercise.repositories

import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import malewicz.jakub.workout_service.dtos.FilterRequest
import malewicz.jakub.workout_service.exceptions.BadRequestException
import malewicz.jakub.workout_service.exercise.entities.Equipment
import malewicz.jakub.workout_service.exercise.entities.ExerciseEntity
import malewicz.jakub.workout_service.exercise.entities.ExerciseType
import malewicz.jakub.workout_service.exercise.entities.MuscleGroup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ExerciseSpecificationTest {

    @Mock
    private lateinit var root: Root<ExerciseEntity>

    @Mock
    private lateinit var criteriaBuilder: CriteriaBuilder


    @Test
    fun `to Predicate should throw BadRequestException when given incorrect filter field`() {
        val filter = FilterRequest("TYPE", "incorrect_field")
        val specification = ExerciseSpecification(listOf(filter))
        assertThrows(BadRequestException::class.java) { specification.toPredicate(root, null, criteriaBuilder) }
    }

    @Test
    fun `to Predicate should throw BadRequestException when given incorrect exercise type`() {
        val filter = FilterRequest("INCORRECT_TYPE", "type")
        val specification = ExerciseSpecification(listOf(filter))
        assertThrows(BadRequestException::class.java) { specification.toPredicate(root, null, criteriaBuilder) }
    }

    @Test
    fun `to Predicate should throw BadRequestException when given incorrect muscle group`() {
        val filter = FilterRequest("INCORRECT_MUSCLE_GROUP", "muscleGroup")
        val specification = ExerciseSpecification(listOf(filter))
        assertThrows(BadRequestException::class.java) { specification.toPredicate(root, null, criteriaBuilder) }
    }

    @Test
    fun `to Predicate should throw BadRequestException when given incorrect equipment`() {
        val filter = FilterRequest("INCORRECT_EQUIPMENT", "equipment")
        val specification = ExerciseSpecification(listOf(filter))
        assertThrows(BadRequestException::class.java) { specification.toPredicate(root, null, criteriaBuilder) }
    }

    @Test
    fun `to Predicate should return Predicate when given correct exercise type`() {
        val filter = FilterRequest(ExerciseType.CARDIO, "type")
        val specification = ExerciseSpecification(listOf(filter))
        val expectedPredicate = Mockito.mock(Predicate::class.java)
        val path = Mockito.mock(Path::class.java)
        `when`(root.get<ExerciseType>(filter.field)).thenReturn(path as Path<ExerciseType>?)
        `when`(criteriaBuilder.equal(path, ExerciseType.CARDIO)).thenReturn(expectedPredicate)
        `when`(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate)
        val res = specification.toPredicate(root, null, criteriaBuilder)
        assertEquals(expectedPredicate, res)
    }

    @Test
    fun `to Predicate should return Predicate when given correct muscle group`() {
        val filter = FilterRequest(MuscleGroup.BICEPS, "muscleGroup")
        val specification = ExerciseSpecification(listOf(filter))
        val expectedPredicate = Mockito.mock(Predicate::class.java)
        val path = Mockito.mock(Path::class.java)
        `when`(root.get<MuscleGroup>(filter.field)).thenReturn(path as Path<MuscleGroup>?)
        `when`(criteriaBuilder.equal(path, MuscleGroup.BICEPS)).thenReturn(expectedPredicate)
        `when`(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate)
        val res = specification.toPredicate(root, null, criteriaBuilder)
        assertEquals(expectedPredicate, res)
    }

    @Test
    fun `to Predicate should return Predicate when given correct equipment`() {
        val filter = FilterRequest(Equipment.DUMBBELL, "equipment")
        val specification = ExerciseSpecification(listOf(filter))
        val expectedPredicate = Mockito.mock(Predicate::class.java)
        val path = Mockito.mock(Path::class.java)
        `when`(root.get<Equipment>(filter.field)).thenReturn(path as Path<Equipment>?)
        `when`(criteriaBuilder.equal(path, Equipment.DUMBBELL)).thenReturn(expectedPredicate)
        `when`(criteriaBuilder.and(expectedPredicate)).thenReturn(expectedPredicate)
        val res = specification.toPredicate(root, null, criteriaBuilder)
        assertEquals(expectedPredicate, res)
    }
}