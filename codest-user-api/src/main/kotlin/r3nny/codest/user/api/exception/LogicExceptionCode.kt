package r3nny.codest.user.api.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.LogicException

enum class LogicExceptionCode(
    override val errorCode: String,
    override val message: String,
) : ExceptionCode<LogicException> {

    USERNAME_ALREADY_EXISTS("UsernameAlreadyExists", "Пользователь уже существует"),
    USER_NOT_FOUND("UserNotFound", "Пользователь не найден"),
    ;

}