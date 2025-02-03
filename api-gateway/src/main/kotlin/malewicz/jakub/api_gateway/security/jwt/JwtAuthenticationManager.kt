package malewicz.jakub.api_gateway.security.jwt

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val token = authentication?.credentials as String?
        if (token != null) {
            return Mono.just(authentication!!)
        } else {
            throw object: AuthenticationException("Invalid JWT token") {}
        }
    }
}