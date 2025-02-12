package malewicz.jakub.workout_service.exercise.controllers

import jakarta.validation.Valid
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.dtos.ExerciseReorderRequest
import malewicz.jakub.workout_service.exercise.services.ExerciseService
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import malewicz.jakub.workout_service.weight.WeightConverter
import malewicz.jakub.workout_service.weight.WeightUnits
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/exercise")
class ExerciseController(
    private val exerciseService: ExerciseService,
    private val weightConverter: WeightConverter
) {
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
  ) = exerciseService.reorderExercises(reorderRequest, userId)

  @DeleteMapping("/{id}")
  fun deleteFromWorkout(@PathVariable id: UUID, @RequestParam workoutId: UUID) =
      exerciseService.deleteFromWorkout(id, workoutId)
}
