package r3nny.codest.shared.exception

abstract class CustomException(
    val exceptionCode: ExceptionCode<out CustomException>,
    override val cause: Throwable? = null,
    customMessage: String?,
) : Exception() {
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

class SecurityException(
    val isAuthorization: Boolean,
    exceptionCode: ExceptionCode<SecurityException>,
    cause: Throwable? = null,
    customMessage: String? = null,
) : CustomException(exceptionCode, cause, customMessage)

fun throwInvocationException(
    code: ExceptionCode<InvocationException>,
    cause: Throwable? = null,
    message: String? = null,
): Nothing {
    throw InvocationException(code, cause, message)
}

fun throwSecurityException(
    code: ExceptionCode<SecurityException>,
    isForbidden: Boolean = false,
    cause: Throwable? = null,
    message: String? = null,
): Nothing {
    throw SecurityException(isForbidden, code, cause, message)
}

fun throwValidationException(
    code: ExceptionCode<ValidationException>,
    cause: Throwable? = null,
    message: String? = null,
): Nothing {
    throw ValidationException(code, cause, message)
}

fun throwLogicException(
    code: ExceptionCode<LogicException>,
    cause: Throwable? = null,
    message: String? = null,
): Nothing {
    throw LogicException(code, cause, message)
}

suspend fun <R> wrap(errorCode: ExceptionCode<InvocationException>, block: suspend () -> R) = runCatching {
    block.invoke()
}.onFailure {
    if (it !is CustomException)
        throwInvocationException(errorCode, it)
}.getOrThrow()