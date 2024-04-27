package r3nny.codest.runner.exception

import r3nny.codest.shared.exception.ExceptionCode
import r3nny.codest.shared.exception.InvocationException

enum class InvocationExceptionCode(
    override val errorCode: String,
    override val message: String,
) : ExceptionCode<InvocationException> {

    FILE_WRITE_ERROR("FileWriteError", "Ошибка при сохранении файла"),
    FILE_READ_ERROR("FileReadError", "Ошибка при чтении файла"),
    TIMEOUT_EXCEPTION("TimeoutException", "Превышено время выполнения"),
    KAFKA_EXCEPTION("KafkaException", "Произошла ошибка в Kafka"),
    FILE_DELETE_ERROR("FileDeleteError", "Ошибка при удалении файла"),
    ;

}