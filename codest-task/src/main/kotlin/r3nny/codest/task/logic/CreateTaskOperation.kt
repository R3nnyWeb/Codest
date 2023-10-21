package r3nny.codest.task.logic

import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter

class CreateTaskOperation(
    val taskAdapter: TaskAdapter
) {
    suspend fun activate(request: CreateTaskRequest) {

    }
}