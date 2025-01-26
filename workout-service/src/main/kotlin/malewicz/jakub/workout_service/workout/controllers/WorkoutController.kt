package malewicz.jakub.workout_service.workout.controllers

import jakarta.validation.Valid
import malewicz.jakub.workout_service.workout.dtos.WorkoutCreateRequest
import malewicz.jakub.workout_service.workout.services.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/workout")
class WorkoutController(private val workoutService: WorkoutService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createWorkout(
        @Valid @RequestBody workoutCreateRequest: WorkoutCreateRequest,
        @RequestHeader("X-User-Id") userId: UUID
    ) = workoutService.createWorkout(workoutCreateRequest, userId)

    @GetMapping("/all")
    fun getUserWorkouts(@RequestHeader("X-User-Id") userId: UUID) = workoutService.getUserWorkouts(userId)

    @GetMapping("/{workoutId}")
    fun getWorkoutDetails(@RequestHeader("X-User-Id") userId: UUID, @PathVariable workoutId: UUID) =
        workoutService.getWorkoutDetails(userId, workoutId)

}