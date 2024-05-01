package r3nny.codest.solution.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.InvocationException

enum class InvocationExceptionCode(
    override val errorCode: String,
    override val message: String,
): ExceptionCode<InvocationException> {
    TASK_API("TaskApiError", "Ошибка при обращении к API задач"),
    ATTEMPTS("AttemptsError", "Ошибка при обращении к таблице attempts"),
    KAFKA("KafkaError", "Ошибка при обращении к таблице attempts"),;
}