package malewicz.jakub.user_service.authentication.controllers

import jakarta.validation.Valid
import malewicz.jakub.user_service.authentication.dtos.ForgotPasswordRequest
import malewicz.jakub.user_service.authentication.dtos.LoginRequest
import malewicz.jakub.user_service.authentication.dtos.RegistrationRequest
import malewicz.jakub.user_service.authentication.dtos.ResetPasswordRequest
import malewicz.jakub.user_service.authentication.services.AuthenticationService
import malewicz.jakub.user_service.authentication.services.PasswordService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
  private val authenticationService: AuthenticationService,
  private val passwordService: PasswordService
) {

  @PostMapping("/login")
  fun login(@Valid @RequestBody loginRequest: LoginRequest) = authenticationService.login(loginRequest)

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  fun register(@Valid @RequestBody registrationRequest: RegistrationRequest) =
    authenticationService.registerUser(registrationRequest)

  @PostMapping("/activate/{userId}")
  fun activateAccount(@PathVariable userId: UUID) =
    authenticationService.activateAccount(userId)

  @PostMapping("/forgot-password")
  fun forgotPassword(@Valid @RequestBody forgotPasswordRequest: ForgotPasswordRequest) =
    passwordService.forgotPassword(forgotPasswordRequest)

  @PostMapping("/reset-password")
  fun resetPassword(@Valid @RequestBody resetPasswordRequest: ResetPasswordRequest) =
    passwordService.resetPassword(resetPasswordRequest)

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @GetMapping
  fun isUserAuthenticated() = Unit
}