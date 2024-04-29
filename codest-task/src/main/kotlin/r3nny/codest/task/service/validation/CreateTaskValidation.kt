package r3nny.codest.task.service.validation

import r3nny.codest.shared.exception.throwValidationException
import r3nny.codest.task.dto.http.CreateTaskRequestDto
import r3nny.codest.task.exception.ValidationExceptionCode

fun validateCreateTask(request: CreateTaskRequestDto) {
    runCatching {
        with(request) {
            if (parameters.inputTypes.isEmpty())
                throw Exception("Не переданы входные параметры")
            if (tests.any {
                    it.inputValues.size != parameters.inputTypes.size
                })
                throw Exception("Количество входных данных в тесте меньше, чем количество входных параметров")
            if (tests.size < 2)
                throw Exception("Количество тестов меньше минимального")
            if (startCode.keys != languages)
                throw Exception("Не для всех языков указан начальный код")
        }
    }.recoverCatching {
        throwValidationException(
            code = ValidationExceptionCode.CREATE_REQUEST_ERROR,
            message = it.message
        )
    }.getOrThrow()
}