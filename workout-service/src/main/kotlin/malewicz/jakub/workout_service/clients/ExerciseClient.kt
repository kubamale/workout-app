package malewicz.jakub.workout_service.clients

import malewicz.jakub.workout_service.exercise.dtos.ExerciseDetails
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient("EXERCISE-SERVICE")
interface ExerciseClient {
  @RequestMapping("/api/v1/exercises", method = [RequestMethod.GET])
  fun getExercisesDetails(@RequestParam ids: List<UUID>): List<ExerciseDetails>
}