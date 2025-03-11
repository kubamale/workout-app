package malewicz.jakub.statistics_service.statistics.controllers

import malewicz.jakub.statistics_service.statistics.services.StatisticsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/statistics")
class StatisticsController(private val statisticsService: StatisticsService) {

  @GetMapping("/overall")
  fun getOverallStatistics(@RequestHeader("X-User-Id") userId: UUID) = statisticsService.getOverallStatistics(userId)

  @GetMapping("/exercise/{id}")
  fun getExerciseStatistics(@RequestHeader("X-User-Id") userId: UUID, @PathVariable("id") exerciseId: UUID) =
    statisticsService.getExerciseStatistics(exerciseId, userId)
}