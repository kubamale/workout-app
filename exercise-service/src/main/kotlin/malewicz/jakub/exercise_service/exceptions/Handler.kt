package malewicz.jakub.exercise_service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.ErrorResponse
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Handler {
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
  fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException) =
      ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message ?: "Method not supported.")

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException::class)
  fun handleResourceNotFoundException(ex: ResourceNotFoundException) =
      ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.message)

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MissingRequestHeaderException::class)
  fun handleMissingRequestHeaderException(ex: MissingRequestHeaderException) =
      ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message)

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(BadRequestException::class)
  fun handleBadRequest(ex: BadRequestException) =
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
}
