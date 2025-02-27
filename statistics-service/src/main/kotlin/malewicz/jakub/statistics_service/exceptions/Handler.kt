package malewicz.jakub.statistics_service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.ErrorResponse
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class Handler {
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException) =
        ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message ?: "Method not supported.")

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [MissingRequestHeaderException::class, MissingServletRequestParameterException::class])
    fun handleMissingRequestHeaderException(ex: MissingRequestValueException) =
        ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message ?: "Parameter not found.")

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleArgumentTypeMismatchException(ex: MethodArgumentTypeMismatchException) =
        ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message)

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ErrorResponse {
        val errorMessage = StringBuilder()
        ex.allErrors.forEach {
            if (it is FieldError) {
                errorMessage.append("${it.field} ${it.defaultMessage}. ")
            } else {
                errorMessage.append(it.defaultMessage)
            }
        }
        return ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, errorMessage.toString().trim())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class)
    fun handleBadRequestException(ex: BadRequestException) =
        ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.message)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException) =
        ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.message)

    @ExceptionHandler
    fun handleHttpException(ex: HttpException): ResponseEntity<ProblemDetail> =
        ResponseEntity(ErrorResponse.create(ex, ex.status, ex.message).body, ex.status)
}