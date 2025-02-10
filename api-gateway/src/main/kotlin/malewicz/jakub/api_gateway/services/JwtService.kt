package malewicz.jakub.api_gateway.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import malewicz.jakub.api_gateway.models.LengthUnits
import malewicz.jakub.api_gateway.models.UserData
import malewicz.jakub.api_gateway.models.WeightUnits
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class JwtService(
    @Value("\${jwt.secret}") private val secret: String,
    @Value("\${jwt.issuer}") private val issuer: String
) {

    private val algorithm: Algorithm = Algorithm.HMAC256(secret)
    private val verifier = JWT.require(algorithm)
        .withIssuer(issuer)
        .build()

    fun extractUserDataFromToken(token: String) : UserData {
        try {
            val decodedJwt = verifier.verify(token)
            val id = UUID.fromString(decodedJwt.subject.toString())
            val email = decodedJwt.getClaim("email").asString()
            val weightUnits = WeightUnits.valueOf(decodedJwt.getClaim("weight_units").asString())
            val lengthUnits = LengthUnits.valueOf(decodedJwt.getClaim("length_units").asString())
            return UserData(id, email, weightUnits, lengthUnits)
        } catch (ex: JWTVerificationException ) {
            throw BadCredentialsException(ex.message, ex)
        } catch (ex: IllegalArgumentException) {
            throw BadCredentialsException(ex.message, ex)
        }
    }
}