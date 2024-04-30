package r3nny.codest.solution.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.ValidationException

enum class ValidationExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<ValidationException> {
    STATUS_NOT_VALID("CreateRequestError", "Невалидное тело запроса")
}