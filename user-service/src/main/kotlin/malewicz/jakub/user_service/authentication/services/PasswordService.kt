package malewicz.jakub.user_service.authentication.services

import malewicz.jakub.user_service.authentication.dtos.ForgotPasswordRequest
import malewicz.jakub.user_service.authentication.dtos.ResetPasswordRequest
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.exceptions.ResourceNotFoundException
import malewicz.jakub.user_service.notification.services.NotificationService
import malewicz.jakub.user_service.user.entities.ResetPasswordEntity
import malewicz.jakub.user_service.user.repositories.ForgotPasswordRepository
import malewicz.jakub.user_service.user.repositories.UserRepository
import org.apache.commons.lang.RandomStringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Service
class PasswordService(
    private val notificationService: NotificationService,
    private val forgotPasswordRepository: ForgotPasswordRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    @Value("\${forgotPassword.token.ttl-seconds}") private val forgotPasswordTokenTTL: Long,
    @Value("\${forgotPassword.token.length}") private val forgotPasswordTokenLength: Int,
) {
    fun forgotPassword(forgotPasswordRequest: ForgotPasswordRequest) {
        val user = userRepository.findByEmail(forgotPasswordRequest.email) ?: return
        val expiration = ZonedDateTime.now(ZoneOffset.UTC).plusSeconds(forgotPasswordTokenTTL)
        val token = RandomStringUtils.randomAlphanumeric(forgotPasswordTokenLength)
        forgotPasswordRepository.save(ResetPasswordEntity(token, expiration, user))
        notificationService.sendResetPasswordEmail(token, user.email)
    }

    fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        val token = forgotPasswordRepository.findByToken(resetPasswordRequest.token)
            .orElseThrow { ResourceNotFoundException("Incorrect token.") }

        if(token.expires.isBefore(ZonedDateTime.now(ZoneOffset.UTC))) {
            throw BadRequestException("Token is expired. Please generate a new one.")
        }

        token.user.apply {
            password = passwordEncoder.encode(resetPasswordRequest.password)
            userRepository.save(this)
        }
    }
}