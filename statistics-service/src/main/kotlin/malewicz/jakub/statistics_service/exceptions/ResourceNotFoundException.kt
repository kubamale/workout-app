package malewicz.jakub.statistics_service.exceptions

import org.springframework.http.HttpStatus

class ResourceNotFoundException(override val message: String, val status: HttpStatus = HttpStatus.NOT_FOUND) :
    RuntimeException(message)