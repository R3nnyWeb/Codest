package r3nny.codest.runner.helper

import r3nny.codest.shared.exception.CustomException
import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.InvocationException
import r3nny.codest.shared.exception.throwInvocationException

suspend fun <R> wrap(code: ExceptionCode<InvocationException>, block: suspend () -> R) = runCatching {
    block.invoke()
}.onFailure {
    if (it !is CustomException)
        throwInvocationException(code, it)
}.getOrThrow()
