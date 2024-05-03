package r3nny.codest.api.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.LogicException

enum class LogicExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<LogicException> {
    TASK_NOT_FOUND("TaskNotFound", "Задача не найдена"),
    LANGUAGE_NOT_ACCEPTABLE("LanguageNotAcceptable", "Не возможно запустить задачу"),
    ATTEMPT_NOT_FOUND("AttemptNotFound", "Попытка не найдена"),;
}