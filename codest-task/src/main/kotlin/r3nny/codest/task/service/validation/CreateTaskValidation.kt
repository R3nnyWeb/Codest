package r3nny.codest.task.service.validation

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.throwValidationException
import r3nny.codest.task.exception.ValidationExceptionCode
import r3nny.codest.task.model.CreateTaskRequest


fun validateCreateTask(request: CreateTaskRequest) {
    runCatching {
        with(request) {
            if (inputTypes.isEmpty())
                throw Exception("Не переданы входные параметры")
            if (tests.any {
                    it.inputData.size != inputTypes.size
                })
                throw Exception("Количество входных данных в тесте неравно количеству входных параметров")
            if (tests.size < 2)
                throw Exception("Количество тестов меньше минимального")
            if (startCodes.keys.toSet() != (languages ?: Language.entries.map { it.name }).toSet())
                throw Exception("Не для всех языков указан начальный код")
        }
    }.recoverCatching {
        throwValidationException(
            code = ValidationExceptionCode.CREATE_REQUEST_ERROR,
            message = it.message
        )
    }.getOrThrow()
}
