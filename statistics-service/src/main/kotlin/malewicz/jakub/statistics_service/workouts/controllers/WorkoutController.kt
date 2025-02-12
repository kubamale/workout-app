package malewicz.jakub.statistics_service.workouts.controllers

import jakarta.validation.Valid
import java.util.UUID
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/workout")
class WorkoutController(private val workoutService: WorkoutService) {

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  fun addWorkout(
      @Valid @RequestBody workout: WorkoutCreateRequest,
      @RequestHeader("X-User-Id") userId: UUID
  ) {
    workoutService.createWorkout(workout, userId)
  }
}
