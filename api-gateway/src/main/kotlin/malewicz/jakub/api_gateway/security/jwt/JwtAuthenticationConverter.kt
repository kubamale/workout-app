package malewicz.jakub.api_gateway.security.jwt

import malewicz.jakub.api_gateway.models.UserData
import malewicz.jakub.api_gateway.services.JwtService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationConverter(val jwtService: JwtService) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        var token = exchange!!.request.headers.getFirst("Authorization")
        if (!token.isNullOrBlank() && token.startsWith("Bearer ")) {
            token = token.substring(7)
            val userData = jwtService.extractUserDataFromToken(token)
            setHeadersFromUserData(userData, exchange)
            return Mono.just(UsernamePasswordAuthenticationToken(userData.id, token, listOf()))
        }
        return Mono.empty()
    }

    private fun setHeadersFromUserData(userData: UserData, exchange: ServerWebExchange) {
        exchange.request.headers.remove("X-User-Id")
        exchange.request.headers.add("X-User-Id", userData.id.toString())
        exchange.request.headers.add("X-Weight-Units", userData.weightUnits.toString())
        exchange.request.headers.add("X-Length-Units", userData.lengthUnits.toString())
    }
}