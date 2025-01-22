package malewicz.jakub.workout_service.workout.dtos

import jakarta.validation.constraints.NotBlank

data class WorkoutCreateRequest(@field:NotBlank val name: String)
