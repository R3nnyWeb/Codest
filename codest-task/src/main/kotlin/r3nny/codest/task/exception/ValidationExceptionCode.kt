package r3nny.codest.task.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.ValidationException

enum class ValidationExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<ValidationException> {
    CREATE_REQUEST_ERROR("CreateRequestError", "Невалидное тело запроса")
}