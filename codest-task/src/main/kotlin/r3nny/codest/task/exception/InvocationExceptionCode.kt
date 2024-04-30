package r3nny.codest.task.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.InvocationException

enum class InvocationExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<InvocationException> {
    CREATE_TASK_ERROR("CreateTaskError", "Ошибка при создании задачи"),
    GET_TASK_ERROR("GetTaskError", "Ошибка при получении задачи"),
    GET_TEST_ERROR("GetTestError", "Ошибка при получении теста"),;
}