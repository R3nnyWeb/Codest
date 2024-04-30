package r3nny.codest.task.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.LogicException

enum class LogicExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<LogicException> {
    TASK_NOT_FOUND("TaskNotFound", "Задача не найдена"),
    INTERNAL_NOT_EXIST("InternalNotExist", "Не возможно запустить задачу"),
}