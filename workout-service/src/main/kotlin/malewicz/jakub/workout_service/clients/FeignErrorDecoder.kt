package malewicz.jakub.workout_service.clients

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Response
import feign.codec.ErrorDecoder
import malewicz.jakub.workout_service.exceptions.HttpException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.stereotype.Component
import java.io.IOException


@Component
class FeignErrorDecoder(val mapper: ObjectMapper) : ErrorDecoder {

  override fun decode(methodKey: String?, response: Response): Exception {
    if (response.status() == 503) return HttpException(HttpStatus.valueOf(response.status()), "Service unavailable")

    try {
      response.body().asInputStream().use { bodyIs ->
        val ex = mapper.readValue(bodyIs, ProblemDetail::class.java)
        return HttpException(HttpStatus.valueOf(response.status()), ex.detail ?: ex.status.toString())
      }
    } catch (e: IOException) {
      return java.lang.Exception(e.message)
    }
  }
}