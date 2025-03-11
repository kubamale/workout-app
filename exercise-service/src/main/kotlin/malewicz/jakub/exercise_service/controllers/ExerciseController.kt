package malewicz.jakub.exercise_service.controllers

import malewicz.jakub.exercise_service.dtos.ExerciseBasicsResponse
import malewicz.jakub.exercise_service.dtos.ExerciseDetails
import malewicz.jakub.exercise_service.dtos.FilterRequest
import malewicz.jakub.exercise_service.dtos.PageableResponse
import malewicz.jakub.exercise_service.services.ExerciseService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/v1/exercises")
class ExerciseController(private val exerciseService: ExerciseService) {

  @PostMapping
  fun getAllExercises(
    @RequestParam size: Int,
    @RequestParam page: Int,
    @RequestBody filters: List<FilterRequest>
  ): PageableResponse<ExerciseBasicsResponse> {
    val pageRequest = PageRequest.of(page, size)
    return exerciseService.getAllExercises(pageRequest, filters)
  }

  @GetMapping("/{id}")
  fun getExerciseDetails(@PathVariable id: UUID): ExerciseDetails = exerciseService.getDetails(id)

  @GetMapping
  fun getExercises(@RequestParam ids: List<UUID>): List<ExerciseDetails> = exerciseService.getAllByIds(ids)

  @GetMapping("/basic")
  fun getBasicExerciseInfo(@RequestParam ids: List<UUID>): List<ExerciseBasicsResponse> =
    exerciseService.getBasicExercisesInformation(ids)
}
