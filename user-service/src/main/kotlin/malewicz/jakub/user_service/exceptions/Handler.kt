package malewicz.jakub.user_service.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.ErrorResponse
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class Handler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException::class, HttpMessageNotReadableException::class)
    fun handleBadRequestException(ex: RuntimeException) =
        ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message ?: "Something went wrong.")

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupportedException(ex: HttpRequestMethodNotSupportedException) =
        ErrorResponse.create(ex, HttpStatus.BAD_REQUEST, ex.message ?: "Method not supported.")

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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFoundException(ex: ResourceNotFoundException) =
        ErrorResponse.create(ex, HttpStatus.NOT_FOUND, ex.message)


}