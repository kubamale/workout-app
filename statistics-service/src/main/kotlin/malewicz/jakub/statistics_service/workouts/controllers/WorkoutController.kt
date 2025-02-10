package malewicz.jakub.statistics_service.workouts.controllers

import jakarta.validation.Valid
import malewicz.jakub.statistics_service.conversion.LengthUnits
import malewicz.jakub.statistics_service.conversion.SetUnitsConverter
import malewicz.jakub.statistics_service.conversion.WeightUnits
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutCreateRequest
import malewicz.jakub.statistics_service.workouts.services.WorkoutService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/workout")
class WorkoutController(private val setUnitsConverter: SetUnitsConverter, private val workoutService: WorkoutService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun addWorkout(
        @Valid @RequestBody workout: WorkoutCreateRequest,
        @RequestHeader("X-User-Id") userId: UUID,
        @RequestHeader("X-Weight-Units") weightUnits: WeightUnits,
        @RequestHeader("X-Length-Units") lengthUnits: LengthUnits
    ) {
        workout.exercises.forEach { exercise ->
            exercise.sets.map {
                setUnitsConverter.convertSetUnits(
                    it,
                    weightUnits,
                    WeightUnits.KG,
                    lengthUnits,
                    LengthUnits.CM
                )
            }
        }
        workoutService.createWorkout(workout, userId)
    }
}