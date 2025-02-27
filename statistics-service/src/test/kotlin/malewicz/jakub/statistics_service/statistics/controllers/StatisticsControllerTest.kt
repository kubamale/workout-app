package malewicz.jakub.statistics_service.statistics.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.statistics.dtos.OverallStatistics
import malewicz.jakub.statistics_service.statistics.services.StatisticsService
import malewicz.jakub.statistics_service.workouts.dtos.WorkoutBasicInfo
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@WebMvcTest(controllers = [StatisticsController::class])
class StatisticsControllerTest(@Autowired private val mockMvc: MockMvc, @Autowired private val mapper: ObjectMapper) {

  @MockitoBean
  lateinit var statisticsService: StatisticsService

  private val measurementDetails = MeasurementDetails(
    weight = 100.0,
    bodyFat = 20.0,
    leftArm = 50.0,
    rightArm = 50.0,
    chest = 100.0,
    waist = 100.0,
    hips = 100.0,
    leftThigh = 100.0,
    rightThigh = 100.0,
    leftCalf = 60.0,
    rightCalf = 60.0,
    shoulders = 100.0,
  )

  @Test
  fun `get overall statistics should return 200 ok with statistics`() {
    val userId = UUID.randomUUID()
    val overAllStatistics =
      OverallStatistics(1, listOf(WorkoutBasicInfo(UUID.randomUUID(), "legs")), listOf(measurementDetails))
    `when`(statisticsService.getOverallStatistics(userId)).thenReturn(overAllStatistics)

    mockMvc.perform(get("/api/v1/statistics/overall").header("X-User-Id", userId.toString())).andExpect(status().isOk)
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(content().string(mapper.writeValueAsString(overAllStatistics)))
  }
}