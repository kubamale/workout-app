package malewicz.jakub.workout_service.exceptions

import org.springframework.http.HttpStatus
import java.lang.RuntimeException

class HttpException(val status: HttpStatus, override val message: String) : RuntimeException(message) {
}