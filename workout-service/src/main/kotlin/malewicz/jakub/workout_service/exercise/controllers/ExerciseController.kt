package malewicz.jakub.workout_service.exercise.controllers

import jakarta.validation.Valid
import malewicz.jakub.workout_service.dtos.FilterRequest
import malewicz.jakub.workout_service.dtos.PageableResponse
import malewicz.jakub.workout_service.exercise.dtos.ExerciseBasicsResponse
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseReorderRequest
import malewicz.jakub.workout_service.exercise.services.ExerciseService
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.weight.WeightConverter
import malewicz.jakub.workout_service.weight.WeightUnits
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/exercise")
class ExerciseController(
    private val exerciseService: ExerciseService,
    private val weightConverter: WeightConverter
) {

    @GetMapping("/{id}")
    fun getDetails(@PathVariable id: UUID) = exerciseService.getExerciseDetails(id)

    @PostMapping
    fun getAllExercises(
        @RequestParam size: Int,
        @RequestParam page: Int,
        @RequestBody filters: List<FilterRequest>
    ): PageableResponse<ExerciseBasicsResponse> {
        val pageRequest = PageRequest.of(page, size)
        return exerciseService.getAllExercises(pageRequest, filters)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/weight")
    fun addWeightExerciseToWorkout(
        @Valid @RequestBody exerciseRequest: ExerciseCreateRequest<WeightSetCreateRequest>,
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits
    ): UUID {
        if (weightUnits != WeightUnits.KG) {
            exerciseRequest.sets.forEach { it.weight = weightConverter.toKilograms(it.weight) }
        }
        return exerciseService.addExerciseToWorkout(exerciseRequest, userId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/time")
    fun addTimeExerciseToWorkout(
        @Valid @RequestBody exerciseRequest: ExerciseCreateRequest<TimeSetCreateRequest>,
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits
    ): UUID {
        if (weightUnits != WeightUnits.KG) {
            exerciseRequest.sets.forEach { it.weight = weightConverter.toKilograms(it.weight) }
        }
        return exerciseService.addExerciseToWorkout(exerciseRequest, userId)
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/distance")
    fun addDistanceExerciseToWorkout(
        @Valid @RequestBody exerciseRequest: ExerciseCreateRequest<DistanceSetCreateRequest>,
        @RequestHeader("X-User-Id") userId: UUID,
    ) = exerciseService.addExerciseToWorkout(exerciseRequest, userId)


    @PatchMapping("/order")
    fun reorderExercises(
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestBody reorderRequest: ExerciseReorderRequest
    ) =
        exerciseService.reorderExercises(reorderRequest, userId)
}