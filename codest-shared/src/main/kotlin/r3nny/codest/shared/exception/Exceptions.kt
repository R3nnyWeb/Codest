package r3nny.codest.shared.exception

import kotlin.Exception

abstract class CustomException(
    open val exceptionCode: ExceptionCode<out CustomException>,
    override val cause: Throwable? = null
): Exception(exceptionCode.message)

interface ExceptionCode<T : CustomException> {
    val errorCode: String
    val message: String
}

data class InvocationException(
    override val exceptionCode: ExceptionCode<InvocationException>,
    override val cause: Throwable? = null
) : CustomException(exceptionCode, cause)

data class LogicException(
    override val exceptionCode: ExceptionCode<LogicException>,
    override val cause: Throwable? = null
) : CustomException(exceptionCode, cause)

fun throwInvocationException(
    code: ExceptionCode<InvocationException>,
    cause: Throwable? = null
) { throw InvocationException(code, cause) }
