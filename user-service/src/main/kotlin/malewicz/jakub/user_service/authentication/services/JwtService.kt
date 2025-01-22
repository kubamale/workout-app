package malewicz.jakub.user_service.authentication.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import malewicz.jakub.user_service.user.entities.UserEntity
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.issuer}") private val issuer: String,
    @Value("\${jwt.expiration}") private val expiration: Long,) {
    private val algorithm: Algorithm = Algorithm.HMAC256(secret)


    fun generateToken(user: UserEntity): String = JWT.create()
        .withIssuer(issuer)
        .withSubject(user.id!!.toString())
        .withClaim("email", user.email)
        .withClaim("weight_units", user.weightUnits.toString().uppercase())
        .withIssuedAt(Date())
        .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * expiration))
        .sign(algorithm)
}