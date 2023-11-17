package r3nny.codest.task.logic

import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.http.AddLanguageToTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import java.util.*

@Service
class AddLanguageToTaskOperation(
    private val driverGenerator: DriverGeneratorService,
    private val taskAdapter: TaskAdapter
) {

    @LogMethod
    suspend fun activate(taskId: UUID, request: AddLanguageToTaskRequest) {
        val task = taskAdapter.getById(taskId) ?: throw RuntimeException("Не найдено")
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

        taskAdapter.update(updatedTask)
    }

}