package malewicz.jakub.user_service.authentication.services

import malewicz.jakub.user_service.authentication.dtos.CredentialsResponse
import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.exceptions.BadRequestException
import malewicz.jakub.user_service.user.mappers.UserMapper
import malewicz.jakub.user_service.user.repositories.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper,
    private val jwtService: JwtService
) {

    fun registerUser(registrationRequest: RegistrationRequest): CredentialsResponse {
        if (userRepository.existsByEmail(registrationRequest.email)) {
            throw BadRequestException("Account with that email already exists.")
        }

        val user = userMapper.toUserEntity(registrationRequest)
        user.password = passwordEncoder.encode(registrationRequest.password)
        return CredentialsResponse(jwtService.generateToken(userRepository.save(user)))
    }

    fun login(loginRequest: LoginRequest): CredentialsResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw BadRequestException("Incorrect email or password.")

        return if (passwordEncoder.matches(loginRequest.password, user.password)) {
            CredentialsResponse(jwtService.generateToken(user))
        } else {
            throw BadRequestException("Incorrect email or password.")
        }
    }
}