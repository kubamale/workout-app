package malewicz.jakub.user_service.authentication.services

import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.user.entities.UserEntity
import malewicz.jakub.user_service.user.entities.WeightUnits
import malewicz.jakub.user_service.user.mappers.UserMapper
import malewicz.jakub.user_service.user.repositories.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.util.*

@ExtendWith(MockitoExtension::class)
class AuthenticationServiceTest {

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Mock
    lateinit var jwtService: JwtService

    @Mock
    lateinit var userMapper: UserMapper

    @InjectMocks
    lateinit var authenticationService: AuthenticationService

    @Test
    fun `register user should throw BadRequestException when email is already taken`() {
        val registrationRequest =
            RegistrationRequest("user@example.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG)
        given(userRepository.existsByEmail(registrationRequest.email)).willReturn(true)
        assertThrows<BadRequestException> { authenticationService.registerUser(registrationRequest) }
    }

    @Test
    fun registerUser_givenCorrectData_returnsToken() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG
        )
        val user = UserEntity(
            null,
            registrationRequest.email,
            registrationRequest.firstName,
            registrationRequest.lastName,
            registrationRequest.password,
            registrationRequest.dateOfBirth,
            registrationRequest.weightUnits
        )
        val savedUser = UserEntity(
            UUID.randomUUID(),
            registrationRequest.email,
            registrationRequest.firstName,
            registrationRequest.lastName,
            registrationRequest.password,
            registrationRequest.dateOfBirth,
            registrationRequest.weightUnits
        )

        given(userRepository.existsByEmail(registrationRequest.email)).willReturn(false)
        given(userMapper.toUserEntity(registrationRequest)).willReturn(user)
        given(passwordEncoder.encode(registrationRequest.password)).willReturn(registrationRequest.password)
        given(userRepository.save(user)).willReturn(savedUser)
        given(jwtService.generateToken(savedUser)).willReturn("token")

        val credentials = authenticationService.registerUser(registrationRequest)
        assertThat(credentials.token).isNotEmpty()
    }

    @Test
    fun `login should throw BadRequestException when user with email was not found`() {
        val loginRequest = LoginRequest("user@example.com", "Password1!")
        `when`(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(null)
        assertThrows<BadRequestException> { authenticationService.login(loginRequest) }
    }

    @Test
    fun `login should throw BadRequestException when passwords do not match`() {
        val loginRequest = LoginRequest("user@example.com", "Password1!")
        `when`(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(
            UserEntity(
                UUID.randomUUID(),
                "user@example.com",
                "Password1!",
                "John",
                "Doe",
                LocalDate.now(),
                WeightUnits.KG
            )
        )
        `when`(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(false)
        assertThrows<BadRequestException> { authenticationService.login(loginRequest) }
    }

    @Test
    fun `login should return token when passed correct login data`() {
        val loginRequest = LoginRequest("user@example.com", "Password1!")
        val user = UserEntity(
            UUID.randomUUID(),
            "user@example.com",
            "Password1!",
            "John",
            "Doe",
            LocalDate.now(),
            WeightUnits.KG
        )
        `when`(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(user)
        `when`(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(true)
        `when`(jwtService.generateToken(user)).thenReturn("token")
        val credentials = authenticationService.login(loginRequest)
        assertThat(credentials.token).isNotEmpty()
    }
}