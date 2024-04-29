package r3nny.codest.shared.exception

import kotlin.Exception

abstract class CustomException(
    val exceptionCode: ExceptionCode<out CustomException>,
    override val cause: Throwable? = null,
    customMessage: String?
): Exception() {
    final override val message: String = buildString {
        append(exceptionCode.message)
        if (customMessage != null) {
            append(" $customMessage")
        }
    }
}

interface ExceptionCode<T : CustomException> {
    val errorCode: String
    val message: String
}

class InvocationException(
    exceptionCode: ExceptionCode<InvocationException>,
    cause: Throwable? = null,
    customMessage: String? = null,
) : CustomException(exceptionCode, cause, customMessage)

class LogicException(
    exceptionCode: ExceptionCode<LogicException>,
    cause: Throwable? = null,
    customMessage: String? = null,
) : CustomException(exceptionCode, cause, customMessage)

class ValidationException(
    exceptionCode: ExceptionCode<ValidationException>,
    cause: Throwable? = null,
    customMessage: String? = null,
) : CustomException(exceptionCode, cause, customMessage)

fun throwInvocationException(
    code: ExceptionCode<InvocationException>,
    cause: Throwable? = null,
    message: String? = null
) { throw InvocationException(code, cause, message) }

fun throwValidationException(
    code: ExceptionCode<ValidationException>,
    cause: Throwable? = null,
    message: String? = null
) { throw ValidationException(code, cause, message) }

fun throwLogicException(
    code: ExceptionCode<LogicException>,
    cause: Throwable? = null,
    message: String? = null
) { throw LogicException(code, cause, message) }
