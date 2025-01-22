package malewicz.jakub.workout_service.exercise.controllers

import malewicz.jakub.workout_service.exercise.services.ExerciseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/exercise")
class ExerciseController(private val exerciseService: ExerciseService) {

    @GetMapping("/{id}")
    fun getDetails(@PathVariable id: UUID) = exerciseService.getExerciseDetails(id)
}