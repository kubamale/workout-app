package malewicz.jakub.statistics_service.exceptions

import org.springframework.http.HttpStatus

class HttpException(val status: HttpStatus, override val message: String) : RuntimeException(message) {
}