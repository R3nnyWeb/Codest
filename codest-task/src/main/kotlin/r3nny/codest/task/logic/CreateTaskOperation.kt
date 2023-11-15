package r3nny.codest.task.logic

import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import r3nny.codest.task.service.validation.validateCreateTask
import java.util.*

@Service
class CreateTaskOperation(
    val taskAdapter: TaskAdapter,
    val driverGenerator: DriverGeneratorService,
) {

    @LogMethod
    suspend fun activate(request: CreateTaskRequest): UUID = with(request) {
        validateCreateTask(this)

        val task = TaskDTO(
            id = UUID.randomUUID(),
            name = name,
            drivers = driverGenerator.generate(this),
            description = description,
            parameters = parameters,
            startCode = startCode,
            tests = tests
        )

        taskAdapter.createTask(task)
    }
}