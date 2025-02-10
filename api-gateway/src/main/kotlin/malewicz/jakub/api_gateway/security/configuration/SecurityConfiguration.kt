package malewicz.jakub.api_gateway.security.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter


@Configuration
class SecurityConfiguration {

    @Bean
    fun securityFilterChain(http: ServerHttpSecurity, authenticationManager: ReactiveAuthenticationManager, authenticationConverter: ServerAuthenticationConverter): SecurityWebFilterChain {
        val authenticationWebFilter = AuthenticationWebFilter(authenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter)

        http.csrf{it.disable()}
            .authorizeExchange{exchange ->
                exchange.pathMatchers("/USER-SERVICE/api/v1/auth/**").permitAll()
                exchange.anyExchange().authenticated()
            }
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        return http.build()
    }
}