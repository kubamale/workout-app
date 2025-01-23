package malewicz.jakub.workout_service.exercise.controllers

import jakarta.validation.Valid
import malewicz.jakub.workout_service.exercise.dtos.ExerciseCreateRequest
import malewicz.jakub.workout_service.exercise.services.ExerciseService
import malewicz.jakub.workout_service.set.dtos.DistanceSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.TimeSetCreateRequest
import malewicz.jakub.workout_service.set.dtos.WeightSetCreateRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/exercise")
class ExerciseController(private val exerciseService: ExerciseService) {

    @GetMapping("/{id}")
    fun getDetails(@PathVariable id: UUID) = exerciseService.getExerciseDetails(id)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/weight")
    fun addWeightExerciseToWorkout(
        @Valid @RequestBody exerciseRequest: ExerciseCreateRequest<WeightSetCreateRequest>,
        @RequestHeader("X-User-Id") userId: UUID
    ) = exerciseService.addExerciseToWorkout(exerciseRequest, userId)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/time")
    fun addTimeExerciseToWorkout(
        @Valid @RequestBody exerciseRequest: ExerciseCreateRequest<TimeSetCreateRequest>,
        @RequestHeader("X-User-Id") userId: UUID
    ) = exerciseService.addExerciseToWorkout(exerciseRequest, userId)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/distance")
    fun addDistanceExerciseToWorkout(
        @Valid @RequestBody exerciseRequest: ExerciseCreateRequest<DistanceSetCreateRequest>,
        @RequestHeader("X-User-Id") userId: UUID
    ) = exerciseService.addExerciseToWorkout(exerciseRequest, userId)
}