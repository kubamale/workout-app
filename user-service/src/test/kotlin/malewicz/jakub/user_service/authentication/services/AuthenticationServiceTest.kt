package malewicz.jakub.user_service.authentication.services

import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.exceptions.ResourceNotFoundException
import malewicz.jakub.user_service.notification.services.NotificationService
import malewicz.jakub.user_service.user.entities.LengthUnits
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
import org.mockito.Mockito.verify
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

    @Mock
    lateinit var notificationService: NotificationService

    @InjectMocks
    lateinit var authenticationService: AuthenticationService

    @Test
    fun `register user should throw BadRequestException when email is already taken`() {
        val registrationRequest =
            RegistrationRequest("user@example.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG, LengthUnits.CM)
        given(userRepository.existsByEmail(registrationRequest.email)).willReturn(true)
        assertThrows<BadRequestException> { authenticationService.registerUser(registrationRequest) }
    }

    @Test
    fun `register user should save new user and send notification`() {
        val registrationRequest = RegistrationRequest(
            "user@example.com", "Password1!", "John", "Doe", LocalDate.now(), WeightUnits.KG, LengthUnits.CM
        )
        val user = UserEntity(
            null,
            registrationRequest.email,
            registrationRequest.firstName,
            registrationRequest.lastName,
            registrationRequest.password,
            registrationRequest.dateOfBirth,
            registrationRequest.weightUnits,
            registrationRequest.lengthUnits
        )
        val savedUser = UserEntity(
            UUID.randomUUID(),
            registrationRequest.email,
            registrationRequest.firstName,
            registrationRequest.lastName,
            registrationRequest.password,
            registrationRequest.dateOfBirth,
            registrationRequest.weightUnits,
            registrationRequest.lengthUnits
        )

        given(userRepository.existsByEmail(registrationRequest.email)).willReturn(false)
        given(userMapper.toUserEntity(registrationRequest)).willReturn(user)
        given(passwordEncoder.encode(registrationRequest.password)).willReturn(registrationRequest.password)
        given(userRepository.save(user)).willReturn(savedUser)

        authenticationService.registerUser(registrationRequest)
        verify(userRepository).save(user)
        verify(notificationService).sendActivateAccountEmail(savedUser.id!!, user.email)
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
                WeightUnits.KG,
                LengthUnits.CM,
                true
            )
        )
        `when`(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(false)
        assertThrows<BadRequestException> { authenticationService.login(loginRequest) }
    }

    @Test
    fun `login should throw BadRequestException when user is not active`() {
        val loginRequest = LoginRequest("user@example.com", "Password1!")
        val user = UserEntity(
            UUID.randomUUID(),
            "user@example.com",
            "Password1!",
            "John",
            "Doe",
            LocalDate.now(),
            WeightUnits.KG,
            LengthUnits.CM,
            false
        )
        `when`(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(user)
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
            WeightUnits.KG,
            LengthUnits.CM,
            true
        )
        `when`(userRepository.findByEmail(ArgumentMatchers.anyString())).thenReturn(user)
        `when`(passwordEncoder.matches(ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(true)
        `when`(jwtService.generateToken(user)).thenReturn("token")
        val credentials = authenticationService.login(loginRequest)
        assertThat(credentials.token).isNotEmpty()
    }

    @Test
    fun `activate account should throw ResourceNotFoundExecption when no user with provided id exists`() {
        val userId = UUID.randomUUID()
        `when`(userRepository.findById(userId)).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { authenticationService.activateAccount(userId) }
    }

    @Test
    fun `activate account should save user`() {
        val userId = UUID.randomUUID()
        val user = UserEntity(
            userId,
            "email@ex.com",
            "John",
            "Doe",
            "Password1!",
            LocalDate.now(),
            WeightUnits.KG,
            LengthUnits.CM,
        )
        `when`(userRepository.findById(userId)).thenReturn(
            Optional.of(
                user
            )
        )

        authenticationService.activateAccount(userId)
        verify(userRepository).save(user)
    }
}