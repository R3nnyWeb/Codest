package r3nny.codest.task.logic

import java.util.UUID
import r3nny.codest.task.dto.dao.Level
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequestDto
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import r3nny.codest.task.service.validation.validateCreateTask
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class CreateTaskOperation(
    private val taskAdapter: TaskAdapter,
    private val driverGenerator: DriverGeneratorService,
) {

    @Log
    open suspend fun activate(request: CreateTaskRequestDto): TaskDTO = with(request) {
        validateCreateTask(this)

        val task = TaskDTO(
            id = UUID.randomUUID(),
            name = name,
            drivers = driverGenerator.generate(this),
            description = description,
            methodName = methodName,
            parameters = parameters,
            startCode = startCode,
            level = Level.EASY,
            tests = tests
        )

        taskAdapter.createTask(task)
        task
    }
}