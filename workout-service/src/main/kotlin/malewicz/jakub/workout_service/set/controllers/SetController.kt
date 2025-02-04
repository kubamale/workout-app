package malewicz.jakub.workout_service.set.controllers

import malewicz.jakub.workout_service.set.dtos.SetDetailsDto
import malewicz.jakub.workout_service.set.services.SetService
import malewicz.jakub.workout_service.weight.WeightConverter
import malewicz.jakub.workout_service.weight.WeightUnits
import org.springframework.web.bind.annotation.*
import java.util.*

@RequestMapping("/api/v1/set")
@RestController
class SetController(private val setService: SetService, private val weightConverter: WeightConverter) {

    @PutMapping("/exercise/{id}")
    fun updateSets(
        @RequestBody sets: List<SetDetailsDto>,
        @PathVariable("id") workoutExerciseId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits
    ) {
        if (weightUnits != WeightUnits.KG) {
            sets.forEach { it.weight = weightConverter.toKilograms(it.weight) }
        }
        setService.updateSets(sets, workoutExerciseId)
    }
}