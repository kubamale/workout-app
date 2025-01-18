package malewicz.jakub.user_service.authentication.controllers

import jakarta.validation.Valid
import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.authentication.services.AuthenticationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(private val authenticationService: AuthenticationService) {

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest) = authenticationService.login(loginRequest)

    @PostMapping("/register")
    fun register(@Valid @RequestBody registrationRequest: RegistrationRequest) =
        authenticationService.registerUser(registrationRequest)
}