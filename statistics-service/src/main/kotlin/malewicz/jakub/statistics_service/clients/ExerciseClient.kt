package malewicz.jakub.statistics_service.clients

import malewicz.jakub.statistics_service.exercises.dtos.ExerciseBasicInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient("EXERCISE-SERVICE", fallback = ExerciseClientFallback::class)
interface ExerciseClient {
  @GetMapping("/api/v1/exercises/basic")
  fun getBasicExercisesInformation(@RequestParam ids: List<UUID>): List<ExerciseBasicInfo>
}

@Component
class ExerciseClientFallback : ExerciseClient {
  override fun getBasicExercisesInformation(ids: List<UUID>): List<ExerciseBasicInfo> {
    return listOf()
  }
}