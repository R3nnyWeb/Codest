package r3nny.codest.api.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.ValidationException

enum class ValidationExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<ValidationException> {
    CREATE_REQUEST_ERROR("CreateRequestError", "Невалидное тело запроса"),
     STATUS_NOT_VALID("StatusNotValid", "Невалидный статус решения")
}