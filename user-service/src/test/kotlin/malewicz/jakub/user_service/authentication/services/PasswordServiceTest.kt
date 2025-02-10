package malewicz.jakub.user_service.authentication.services

import malewicz.jakub.user_service.authentication.dtos.ForgotPasswordRequest
import malewicz.jakub.user_service.authentication.dtos.ResetPasswordRequest
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.exceptions.ResourceNotFoundException
import malewicz.jakub.user_service.notification.services.NotificationService
import malewicz.jakub.user_service.user.entities.LengthUnits
import malewicz.jakub.user_service.user.entities.ResetPasswordEntity
import malewicz.jakub.user_service.user.entities.UserEntity
import malewicz.jakub.user_service.user.entities.WeightUnits
import malewicz.jakub.user_service.user.repositories.ForgotPasswordRepository
import malewicz.jakub.user_service.user.repositories.UserRepository
import org.apache.commons.lang.RandomStringUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class PasswordServiceTest {
    @Mock
    private lateinit var notificationService: NotificationService

    @Mock
    private lateinit var forgotPasswordRepository: ForgotPasswordRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    private lateinit var passwordService: PasswordService

    private var forgotPasswordTokenTTL: Long = 3600

    private var forgotPasswordTokenLength: Int = 8

    @BeforeEach
    fun setup() {
        passwordService = PasswordService(
            notificationService,
            forgotPasswordRepository,
            userRepository,
            passwordEncoder,
            forgotPasswordTokenTTL,
            forgotPasswordTokenLength
        )
    }

    @Test
    fun `forgot password should return when no user found with email`() {
        val request = ForgotPasswordRequest("test@ex.com")
        `when`(userRepository.findByEmail(request.email)).thenReturn(null)
        passwordService.forgotPassword(request)
        verify(forgotPasswordRepository, times(0)).save(any())
    }

    @Test
    fun `forgot password should save token and send notification`() {
        val user = UserEntity(
            UUID.randomUUID(),
            "test@ex.com",
            "john",
            "Doe",
            "Password1!",
            LocalDate.now(),
            WeightUnits.LB,
            LengthUnits.CM,
            true
        )
        val request = ForgotPasswordRequest(user.email)
        `when`(userRepository.findByEmail(request.email)).thenReturn(user)
        val randomStringMock: MockedStatic<RandomStringUtils> = mockStatic(RandomStringUtils::class.java)
        randomStringMock.use {
            `when`(RandomStringUtils.randomAlphanumeric(forgotPasswordTokenLength)).thenReturn("token")
            passwordService.forgotPassword(request)
            verify(forgotPasswordRepository,).save(any())
            verify(notificationService).sendResetPasswordEmail("token", user.email)
        }
    }

    @Test
    fun `reset password should throw ResourceNotFoundException if token does not exist`() {
        val request = ResetPasswordRequest("token", "Password1!")
        `when`(forgotPasswordRepository.findByToken(request.token)).thenReturn(Optional.empty())
        assertThrows<ResourceNotFoundException> { passwordService.resetPassword(request) }
    }

    @Test
    fun `reset password should throw BadRequestException if token is expired`() {
        val user = UserEntity(
            UUID.randomUUID(),
            "test@ex.com",
            "john",
            "Doe",
            "Password1!",
            LocalDate.now(),
            WeightUnits.LB,
            LengthUnits.CM,
            true
        )
        val request = ResetPasswordRequest("token", "Password1!")
        val token = ResetPasswordEntity(UUID.randomUUID(), "token", ZonedDateTime.now(ZoneOffset.UTC).minusDays(1), user)
        `when`(forgotPasswordRepository.findByToken(request.token)).thenReturn(Optional.of(token))
        assertThrows<BadRequestException> { passwordService.resetPassword(request) }
    }

    @Test
    fun `reset password should save user with new password`() {
        val user = UserEntity(
            UUID.randomUUID(),
            "test@ex.com",
            "john",
            "Doe",
            "Password1!",
            LocalDate.now(),
            WeightUnits.LB,
            LengthUnits.CM,
            true
        )
        val request = ResetPasswordRequest("token", "Password1!")
        val token = ResetPasswordEntity(UUID.randomUUID(), "token", ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(forgotPasswordTokenTTL), user)
        `when`(forgotPasswordRepository.findByToken(request.token)).thenReturn(Optional.of(token))
        `when`(passwordEncoder.encode(request.password)).thenReturn("NewPassword1!")
        passwordService.resetPassword(request)
        assertThat(user.password).isEqualTo("NewPassword1!")
        verify(userRepository).save(user)
    }
}