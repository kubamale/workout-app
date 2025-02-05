package malewicz.jakub.user_service.authentication.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import malewicz.jakub.user_service.authentication.dtos.*
import malewicz.jakub.user_service.authentication.services.AuthenticationService
import malewicz.jakub.user_service.authentication.services.PasswordService
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.exceptions.ResourceNotFoundException
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
import java.util.*

@ExtendWith(MockitoExtension::class)
@WebMvcTest(controllers = [AuthenticationController::class])
class AuthenticationControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val mapper: ObjectMapper,
) {

    @MockitoBean
    private lateinit var authenticationService: AuthenticationService

    @MockitoBean
    private lateinit var passwordService: PasswordService

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
    fun `registerUser should return 201 when passed correct registration data`() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG
        )

        mockMvc.perform(
            post("/api/v1/auth/register").content(mapper.writeValueAsString(registrationRequest)).contentType(
                MediaType.APPLICATION_JSON
            )
        )
            .andExpect(status().isCreated)
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

    @Test
    fun `activateAccount should return 200 when activated account data`() {
        mockMvc.perform(
            post("/api/v1/auth/activate/${UUID.randomUUID()}")
        ).andExpect(status().isOk)
    }

    @Test
    fun `activateAccount should return 404 when no user was found`() {
        val userId = UUID.randomUUID()
        `when`(authenticationService.activateAccount(userId)).thenThrow(ResourceNotFoundException::class.java)
        mockMvc.perform(
            post("/api/v1/auth/activate/${userId}")
        ).andExpect(status().isNotFound)
    }

    @Test
    fun `forgot password should return 400 when passed incorrect email format`() {
        val requestBody = ForgotPasswordRequest("bademail.com")
        mockMvc.perform(
            post("/api/v1/auth/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }


    @Test
    fun `forgot password should return 200 when passed correct email format`() {
        val requestBody = ForgotPasswordRequest("test@ex.com")
        mockMvc.perform(
            post("/api/v1/auth/forgot-password").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `reset password should return 400 when passed empty token`() {
        val requestBody = ResetPasswordRequest("", "Password1!")
        mockMvc.perform(
            post("/api/v1/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `reset password should return 400 when passed incorrect password format`() {
        val requestBody = ResetPasswordRequest("token", "password")
        mockMvc.perform(
            post("/api/v1/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
    }

    @Test
    fun `reset password should return 200 when passed correct data`() {
        val requestBody = ResetPasswordRequest("token", "Password1!")
        mockMvc.perform(
            post("/api/v1/auth/reset-password").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isOk)
    }
}