package r3nny.codest.task.logic

import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.service.validation.validateCreateTask
import java.util.*

class CreateTaskOperation(
    val taskAdapter: TaskAdapter,
) {
    suspend fun activate(request: CreateTaskRequest) : UUID {
        validateCreateTask(request)
        return UUID.randomUUID()
    }
}