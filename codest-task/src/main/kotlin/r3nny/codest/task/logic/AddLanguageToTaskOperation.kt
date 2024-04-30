package r3nny.codest.task.logic

import r3nny.codest.task.dto.common.TaskParameters
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.http.AddLanguageToTaskRequest
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.util.*

@Component
open class AddLanguageToTaskOperation(
    private val driverGenerator: DriverGeneratorService,
    private val taskAdapter: TaskAdapter
) {


    @Log
    open suspend fun activate(taskId: UUID, request: AddLanguageToTaskRequest) {
        val task : TaskDto = taskAdapter.getFullById(taskId) ?: throw RuntimeException("Не найдено")

        if (task.drivers.containsKey(request.language))
            throw RuntimeException("Язык есть, епта")

        val newDriver = driverGenerator.generate(
            methodName = task.methodName,
            parameters = TaskParameters(
                task.inputTypes, task.outputType
            ),
            languages = setOf(request.language)
        )

        val updatedTask = task.copy(
            isEnabled = false,
            drivers = task.drivers + newDriver
        )

//        taskAdapter.updateLanguage(updatedTask)
    }

}