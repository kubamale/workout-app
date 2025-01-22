package malewicz.jakub.user_service.exceptions

import org.springframework.http.HttpStatus

class BadRequestException(override val message: String, val status: HttpStatus = HttpStatus.BAD_REQUEST) : RuntimeException(message)