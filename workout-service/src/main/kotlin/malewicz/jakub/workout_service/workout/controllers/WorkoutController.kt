package malewicz.jakub.workout_service.workout.controllers

import jakarta.validation.Valid
import malewicz.jakub.workout_service.weight.WeightConverter
import malewicz.jakub.workout_service.weight.WeightUnits
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.dtos.WorkoutDetailsResponse
import malewicz.jakub.workout_service.workout.services.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/workout")
class WorkoutController(
    private val workoutService: WorkoutService,
    private val weightConverter: WeightConverter
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createWorkout(
        @Valid @RequestBody workoutCreateRequest: WorkoutCreateRequest,
        @RequestHeader("X-User-Id") userId: UUID
    ) = workoutService.createWorkout(workoutCreateRequest, userId)

    @GetMapping("/all")
    fun getUserWorkouts(@RequestHeader("X-User-Id") userId: UUID) = workoutService.getUserWorkouts(userId)

    @GetMapping("/{workoutId}")
    fun getWorkoutDetails(
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits,
        @PathVariable workoutId: UUID,
    ): WorkoutDetailsResponse {
        val workoutDetails = workoutService.getWorkoutDetails(userId, workoutId)
        if (weightUnits != WeightUnits.KG) {
            workoutDetails.exercises.forEach { exercise ->
                exercise.sets.forEach {
                    it.weight = weightConverter.fromKilograms(it.weight)
                }
            }
        }
        return workoutDetails
    }

}