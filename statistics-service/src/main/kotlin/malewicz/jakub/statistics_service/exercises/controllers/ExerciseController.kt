package malewicz.jakub.statistics_service.exercises.controllers

import malewicz.jakub.statistics_service.exercises.services.ExerciseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/exercises")
class ExerciseController(private val exerciseService: ExerciseService) {

  @GetMapping
  fun getUsersCompletedExercises(@RequestHeader("X-User-Id") userId: UUID) = exerciseService.getUsersExercises(userId)
}