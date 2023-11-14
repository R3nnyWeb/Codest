package r3nny.codest.task.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.dto.ErrorDto
import r3nny.codest.shared.exception.ValidationException

@ControllerAdvice
class RestExceptionHandler {

    @LogMethod
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(e: ValidationException): ResponseEntity<ErrorDto> {
        return ResponseEntity(
            ErrorDto(
                e.message, statusCode = HttpStatus.BAD_REQUEST.value()
            ), HttpStatus.BAD_REQUEST
        )
    }

    @LogMethod
    @ExceptionHandler(HttpMessageNotReadableException::class, MissingServletRequestParameterException::class)
    fun handleInternalValid(e: HttpMessageNotReadableException): ResponseEntity<ErrorDto> =
        ResponseEntity(ErrorDto(e.localizedMessage, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST)

    @LogMethod
    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<ErrorDto> = ResponseEntity(
        ErrorDto(
            e.message ?: "Internal server error", statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value()
        ), HttpStatus.INTERNAL_SERVER_ERROR
    )
}
