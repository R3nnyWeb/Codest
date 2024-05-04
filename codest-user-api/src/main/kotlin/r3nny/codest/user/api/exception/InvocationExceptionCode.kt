package r3nny.codest.user.api.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.InvocationException

enum class InvocationExceptionCode(
    override val errorCode: String,
    override val message: String,
) : ExceptionCode<InvocationException> {

    USER_ERROR("UserError", "Ошибка при обращении к таблице users"),
    ;

}