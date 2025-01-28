package malewicz.jakub.user_service.authentication.services

import malewicz.jakub.user_service.authentication.dtos.CredentialsResponse
import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.exceptions.ResourceNotFoundException
import malewicz.jakub.user_service.notification.services.NotificationService
import malewicz.jakub.user_service.user.mappers.UserMapper
import malewicz.jakub.user_service.user.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper,
    private val jwtService: JwtService,
    private val notificationService: NotificationService
) {

    fun registerUser(registrationRequest: RegistrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.email)) {
            throw BadRequestException("Account with that email already exists.")
        }

        val user = userMapper.toUserEntity(registrationRequest)
        user.password = passwordEncoder.encode(registrationRequest.password)
        val savedUser = userRepository.save(user)
        notificationService.sendActivateAccountEmail(savedUser.id!!, user.email)
    }

    fun login(loginRequest: LoginRequest): CredentialsResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw BadRequestException("Incorrect email or password.")

        return if (!user.active) {
            throw BadRequestException("Account is not active. Please activate your account by accessing link provided in your email.")
        } else if (passwordEncoder.matches(loginRequest.password, user.password)) {
            CredentialsResponse(jwtService.generateToken(user))
        } else {
            throw BadRequestException("Incorrect email or password.")
        }
    }

    fun activateAccount(userId: UUID) {
        val user =
            userRepository.findById(userId).orElseThrow { ResourceNotFoundException("No user with id $userId exists.") }
        user.active = true
        userRepository.save(user)
    }
}