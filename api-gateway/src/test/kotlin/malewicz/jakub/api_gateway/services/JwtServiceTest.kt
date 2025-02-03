package malewicz.jakub.api_gateway.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import malewicz.jakub.api_gateway.models.UserData
import malewicz.jakub.api_gateway.models.WeightUnits
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.security.authentication.BadCredentialsException
import java.util.*

class JwtServiceTest {

    private val jwtSecret =
        "7298e224e282979780e9f79c2607fac71d74dd9d7e25a99480b5fe83b0279db32e5dc329f05b91a6b6cc7922ff8d65e8dcc3f7db0c379cf779beb4e0e96574b58b660818e9727371d35575dac998f0e96e7597408976de51f94d8eab5bad860d474b4c746f214eaec96e76211f36647bd0a9d75c455d76f52deff22fea0c346e1ec5a4d9e75f3a5473be7e6ff1e80a5cefc5e528b48854237e778cccb09174f0cad7605a235da2a86c9e74ef214effcd5b44d866e25b7116b4d992e2b55d618ded7c1ca265011371b80615dd4caf9a0618728ed046e641776d81bdcbc27e7e7ce1cd7babac66e560e70a5d4f85bfdb247f3bb4fd099e0721156807553499f9cc"
    private val jwtIssuer = "test-issuer"
    private val jwtService = JwtService(jwtSecret, jwtIssuer)
    private val algorithm: Algorithm = Algorithm.HMAC256(jwtSecret)

    @Test
    fun `extract user data from token should return user data`() {
        val userData = UserData(UUID.randomUUID(), "johndoe@ex.com", WeightUnits.KG)
        val token = JWT.create()
            .withIssuer(jwtIssuer)
            .withSubject(userData.id.toString())
            .withClaim("email", userData.email)
            .withClaim("weight_units", userData.weightUnits.toString().uppercase())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .sign(algorithm)

        assertThat(jwtService.extractUserDataFromToken(token)).isEqualTo(userData)
    }

    @Test
    fun `extract user data from token should throw BadCredentialsException when incorrect user id passed`() {
        val userData = UserData(UUID.randomUUID(), "johndoe@ex.com", WeightUnits.KG)
        val token = JWT.create()
            .withIssuer(jwtIssuer)
            .withSubject("incorrect-user-id")
            .withClaim("email", userData.email)
            .withClaim("weight_units", userData.weightUnits.toString().uppercase())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .sign(algorithm)

        assertThrows(BadCredentialsException::class.java, {jwtService.extractUserDataFromToken(token)})
    }

    @Test
    fun `extract user data from token should throw BadCredentialsException when expired token passed`() {
        val userData = UserData(UUID.randomUUID(), "johndoe@ex.com", WeightUnits.KG)
        val token = JWT.create()
            .withIssuer(jwtIssuer)
            .withSubject(userData.id.toString())
            .withClaim("email", userData.email)
            .withClaim("weight_units", userData.weightUnits.toString().uppercase())
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() - 1000 * 60 * 10))
            .sign(algorithm)

        assertThrows(BadCredentialsException::class.java, {jwtService.extractUserDataFromToken(token)})
    }


}