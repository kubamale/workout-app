package malewicz.jakub.statistics_service.measurements.controllers

import malewicz.jakub.statistics_service.TestcontainersConfiguration
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.repositories.MeasurementRepository
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime
import java.util.*
import kotlin.test.Test

@ActiveProfiles("test")
@Import(TestcontainersConfiguration::class)
@Sql(scripts = ["/scripts/clear.sql", "/scripts/init.sql"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ITGMeasurementsTest(
  @Autowired private val restTemplate: TestRestTemplate,
  @Autowired private val measurementRepository: MeasurementRepository
) {
  private val userId = UUID.fromString("ad78bab6-2b61-471e-ae9e-e70a4ccb242b")

  @Test
  fun `add measurement should save new measurement`() {
    val initialMeasurementsAmount = measurementRepository.count()
    val measurementDetails = MeasurementDetails(LocalDateTime.now(), 75.0)
    val httpEntity = HttpEntity(measurementDetails, getUserIdInHeader())
    val result = restTemplate.exchange("/api/v1/measurements", HttpMethod.POST, httpEntity, Unit::class.java)
    assertThat(result.statusCode).isEqualTo(HttpStatus.CREATED)
    assertThat(measurementRepository.count()).isEqualTo(initialMeasurementsAmount + 1)
  }

  @Test
  fun `get latest measurement for user`() {
    val latestMeasurement = measurementRepository.findFirstByUserIdOrderByDateDesc(userId)
    val httpEntity = HttpEntity(null, getUserIdInHeader())
    val result =
      restTemplate.exchange("/api/v1/measurements/latest", HttpMethod.GET, httpEntity, MeasurementDetails::class.java)
    assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(result.body).isNotNull
    assertThat(result.body!!.date).isEqualTo(latestMeasurement?.date)
  }

  @Test
  fun `get measurements for user since`() {
    val date = LocalDateTime.of(2025, 1, 12, 12, 12, 12)
    val httpEntity = HttpEntity(null, getUserIdInHeader())
    val result =
      restTemplate.exchange("/api/v1/measurements?from=$date", HttpMethod.GET, httpEntity, Array<MeasurementDetails>::class.java)
    assertThat(result.statusCode).isEqualTo(HttpStatus.OK)
    assertThat(result.body).isNotNull
    assertThat(result.body!!.size).isEqualTo(2)
  }

  private fun getUserIdInHeader() = HttpHeaders().apply { set("X-User-Id", userId.toString()) }

}