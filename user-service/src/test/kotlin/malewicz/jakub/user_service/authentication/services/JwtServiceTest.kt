package malewicz.jakub.user_service.authentication.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import malewicz.jakub.user_service.user.entities.UserEntity
import malewicz.jakub.user_service.user.entities.WeightUnits
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.*


class JwtServiceTest {
    private val secret =
        "0656a82221b569a024ce85601a31724efadc967477913d945d1cc791bb4e9493747af15a33220ea8be4c853da269a1b016c3479888852ee741494dd4d2f209d3a3e3ad2ec4c4c5c1c2169a4a23773ef0bbc578f2b1eb307608a236dc2b22bee62a660c03eebd8303cb5ec5769a84db9d2acf8dc7f56baa9b808e9df1796670cefd436b50cb599eaef969d72956b87fa77f18e412a4cdf87b521d89112a443b150f8191886cd05f00efebacee95e729680c88048bae6fcd2bfecb3890cec2ccd99caef47738179c64d95707506402498aa9781aaf15ffafa75ebd22e11ff56b33463048c686bd7473561300b959b1fbad4699413bf5fabeebba07148766817e17"
    private val jwtService: JwtService = JwtService(secret, "malewicz.jakub", 10)
    private val verifier = JWT.require(Algorithm.HMAC256(secret)).build()

    @Test
    fun `generateToken should return token when passed UserEntity`() {
        val user = UserEntity(
            UUID.randomUUID(),
            "user@example.com",
            "Password1!",
            "John",
            "Doe",
            LocalDate.now(),
            WeightUnits.KG
        )
        val token = jwtService.generateToken(user)
        try {
            val decodedJWT = verifier.verify(token)
            val claims = decodedJWT.claims
            assertThat(decodedJWT.subject).isEqualTo(user.id.toString())
            assertThat(claims["email"]!!.asString()).isEqualTo(user.email)
            assertThat(claims["weight_units"]!!.asString()).isEqualTo(user.weightUnits.toString().uppercase())
            assertThat(decodedJWT.expiresAt.time).isGreaterThan(Date().time)
        } catch (e: JWTVerificationException) {
            println(e.message)
        }
    }
}