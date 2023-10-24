package r3nny.codest.task.service.validation

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.ValidationException
import r3nny.codest.task.dto.http.CreateTaskRequest

fun validateCreateTask(request: CreateTaskRequest){
    with(request) {
            if (parameters.inputTypes.isEmpty())
                throw ValidationException("Не переданы входные параметры")
            if (tests.any {
                    it.inputValues.size != parameters.inputTypes.size
                })
                throw ValidationException("Количество входных данных в тесте меньше, чем количество входных параметров")
            if (tests.size < 2)
                throw ValidationException("Количество тестов меньше минимального")
            if(startCode.size != Language.values().size)
                throw ValidationException("Не для всех языков указан пример")
        }
}