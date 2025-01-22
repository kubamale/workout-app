package malewicz.jakub.user_service.authentication.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.user_service.authentication.dtos.CredentialsResponse
import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.authentication.services.AuthenticationService
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.user.entities.WeightUnits
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
@WebMvcTest(controllers = [AuthenticationController::class])
class AuthenticationControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
) {

    @MockitoBean
    private lateinit var authenticationService: AuthenticationService

    @Test
    fun `login should return 200 and token when passed correct login data`() {
        val loginRequest = LoginRequest("user@ex.com", "Password1!")
        `when`(authenticationService.login(loginRequest)).thenReturn(CredentialsResponse("token"))
        mockMvc.perform(
            post("/api/v1/auth/login").content(mapper.writeValueAsString(loginRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.token").value("token"))
    }

    @Test
    fun `login should return 400 when passed incorrect credentials`() {
        val loginRequest = LoginRequest("user@ex.com", "Password1!")
        `when`(authenticationService.login(loginRequest)).thenThrow(BadRequestException::class.java)
        mockMvc.perform(
            post("/api/v1/auth/login").content(mapper.writeValueAsString(loginRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `registerUser should return 201 and token when passed correct registration data`() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG
        )

        `when`(authenticationService.registerUser(registrationRequest)).thenReturn(CredentialsResponse("token"))
        mockMvc.perform(
            post("/api/v1/auth/register").content(mapper.writeValueAsString(registrationRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.token").value("token"))
    }

    @Test
    fun `registerUser should return 400 when passed email in incorrect format`() {
        val registrationRequest = RegistrationRequest(
            "userexample.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG
        )

        mockMvc.perform(
            post("/api/v1/auth/register").content(mapper.writeValueAsString(registrationRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `registerUser should return 400 when passed password in incorrect format`() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password", "John", "Doe", LocalDate.now(), WeightUnits.KG
        )

        mockMvc.perform(
            post("/api/v1/auth/register").content(mapper.writeValueAsString(registrationRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `registerUser should return 400 when passed empty first name`() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password", "", "Doe", LocalDate.now(), WeightUnits.KG
        )

        mockMvc.perform(
            post("/api/v1/auth/register").content(mapper.writeValueAsString(registrationRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `registerUser should return 400 when passed empty last name`() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password", "John", "", LocalDate.now(), WeightUnits.KG
        )

        mockMvc.perform(
            post("/api/v1/auth/register").content(mapper.writeValueAsString(registrationRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }
}