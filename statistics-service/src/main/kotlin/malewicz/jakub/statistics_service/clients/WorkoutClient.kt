package malewicz.jakub.statistics_service.clients

import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@FeignClient(name = "WORKOUT-SERVICE")
interface WorkoutClient {

  @GetMapping("/api/v1/workout")
  fun findAllByIds(@RequestParam ids: List<UUID>): List<WorkoutBasicInfo>
}