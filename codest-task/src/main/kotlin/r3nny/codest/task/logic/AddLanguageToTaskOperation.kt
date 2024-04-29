package r3nny.codest.task.logic

import java.util.UUID
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.AddLanguageToTaskRequest
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class AddLanguageToTaskOperation(
    private val driverGenerator: DriverGeneratorService,
    private val taskAdapter: TaskAdapter
) {


    @Log
    open suspend fun activate(taskId: UUID, request: AddLanguageToTaskRequest) {
        val task : TaskDTO = taskAdapter.getById(taskId) ?: throw RuntimeException("Не найдено")

        if (task.drivers.containsKey(request.language))
            throw RuntimeException("Язык есть, епта")

        val newDriver = driverGenerator.generate(
            methodName = task.methodName,
            parameters = task.parameters,
            languages = setOf(request.language)
        )

        val updatedTask = task.copy(
            enabled = false,
            drivers = task.drivers + newDriver
        )

        taskAdapter.updateLanguage(updatedTask)
    }

}