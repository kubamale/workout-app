package malewicz.jakub.workout_service.set.controllers

import java.util.*
import malewicz.jakub.workout_service.set.dtos.SetDetailsDto
import malewicz.jakub.workout_service.set.services.SetService
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/set")
@RestController
class SetController(private val setService: SetService) {

  @PutMapping("/exercise/{id}")
  fun updateSets(
      @RequestBody sets: List<SetDetailsDto>,
      @PathVariable("id") workoutExerciseId: UUID
  ) {
    setService.updateSets(sets, workoutExerciseId)
  }
}
