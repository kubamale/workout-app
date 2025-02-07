package malewicz.jakub.statistics_service.measurements.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.statistics_service.conversion.LengthUnits
import malewicz.jakub.statistics_service.conversion.MeasurementUnitsConverter
import malewicz.jakub.statistics_service.conversion.WeightUnits
import malewicz.jakub.statistics_service.exceptions.ResourceNotFoundException
import malewicz.jakub.statistics_service.measurements.dtos.MeasurementDetails
import malewicz.jakub.statistics_service.measurements.services.MeasurementService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@WebMvcTest(controllers = [MeasurementController::class])
class MeasurementControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val mapper: ObjectMapper
) {

    @MockitoBean
    private lateinit var measurementService: MeasurementService

    @MockitoBean
    private lateinit var measurementUnitsConverter: MeasurementUnitsConverter

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
    fun `add measurements should return 400 whe missing X-User-Id header`() {
        mockMvc.perform(
            post("/api/v1/measurements").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(measurementDetails)).header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `add measurements should return 400 whe missing X-Weight-Units header`() {
        mockMvc.perform(
            post("/api/v1/measurements").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(measurementDetails)).header("X-User-Id", UUID.randomUUID())
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `add measurements should return 400 whe missing X-Length-Units header`() {
        mockMvc.perform(
            post("/api/v1/measurements").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(measurementDetails)).header("X-User-Id", UUID.randomUUID())
                .header("X-Weight-Units", WeightUnits.KG)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `add measurements should return 200 when saved measurement`() {
        val userId = UUID.randomUUID()
        `when`(
            measurementUnitsConverter.convertMeasurementUnits(
                measurementDetails,
                WeightUnits.KG,
                WeightUnits.KG,
                LengthUnits.CM,
                LengthUnits.CM
            )
        ).thenReturn(measurementDetails)
        mockMvc.perform(
            post("/api/v1/measurements").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(measurementDetails))
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `add measurements should return 400 when passed data has negative values`() {
        val details = measurementDetails.copy(weight = -1.0)
        val userId = UUID.randomUUID()
        `when`(
            measurementUnitsConverter.convertMeasurementUnits(
                measurementDetails,
                WeightUnits.KG,
                WeightUnits.KG,
                LengthUnits.CM,
                LengthUnits.CM
            )
        ).thenReturn(measurementDetails)
        mockMvc.perform(
            post("/api/v1/measurements").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(details))
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `get measurements should return 404 when no saved measurement for user`() {
        val userId = UUID.randomUUID()
        `when`(measurementService.getLatestMeasurement(userId)).thenThrow(ResourceNotFoundException::class.java)
        mockMvc.perform(
            get("/api/v1/measurements/latest")
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `get measurements should return 200 when measurement was found`() {
        val userId = UUID.randomUUID()
        `when`(measurementService.getLatestMeasurement(userId)).thenReturn(measurementDetails)
        `when`(
            measurementUnitsConverter.convertMeasurementUnits(
                measurementDetails,
                WeightUnits.KG,
                WeightUnits.KG,
                LengthUnits.CM,
                LengthUnits.CM
            )
        ).thenReturn(measurementDetails)
        mockMvc.perform(
            get("/api/v1/measurements/latest")
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(measurementDetails)))
    }

    @Test
    fun `get measurements since should return 400 when incorrect parameter passed`() {
        val userId = UUID.randomUUID()
        mockMvc.perform(
            get("/api/v1/measurements")
                .param("wrong", LocalDateTime.now().toString())
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `get measurements since should return 400 when passed incorrect format of date`() {
        val userId = UUID.randomUUID()
        mockMvc.perform(
            get("/api/v1/measurements")
                .param("from", LocalDate.now().toString())
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `get measurements since should return 200 when passed correct data`() {
        val userId = UUID.randomUUID()
        val fromDate = LocalDateTime.now()
        `when`(measurementService.getMeasurementsSince(userId, fromDate)).thenReturn(mutableListOf(measurementDetails))
        `when`(
            measurementUnitsConverter.convertMeasurementUnits(
                measurementDetails,
                WeightUnits.KG,
                WeightUnits.KG,
                LengthUnits.CM,
                LengthUnits.CM
            )
        ).thenReturn(measurementDetails)


        mockMvc.perform(
            get("/api/v1/measurements")
                .param("from", fromDate.toString())
                .header("X-User-Id", userId)
                .header("X-Weight-Units", WeightUnits.KG)
                .header("X-Length-Units", LengthUnits.CM)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(mapper.writeValueAsString(mutableListOf(measurementDetails))))
    }
}