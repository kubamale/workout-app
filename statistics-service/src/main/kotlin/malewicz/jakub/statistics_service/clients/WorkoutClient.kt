package malewicz.jakub.statistics_service.clients

import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import org.springframework.cloud.openfeign.FallbackFactory
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

@FeignClient(name = "WORKOUT-SERVICE", fallbackFactory = WorkoutClientFallback::class)
interface WorkoutClient {

  @GetMapping("/api/v1/workout")
  fun findAllByIds(@RequestParam ids: List<UUID>): List<WorkoutBasicInfo>
}

@Component
class WorkoutClientFallback : FallbackFactory<WorkoutClient> {
  override fun create(cause: Throwable?): WorkoutClient {
    return object : WorkoutClient {
      override fun findAllByIds(ids: List<UUID>): List<WorkoutBasicInfo> {
        return listOf()
      }
    }
  }
}