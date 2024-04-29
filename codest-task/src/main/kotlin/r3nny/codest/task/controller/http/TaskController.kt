package r3nny.codest.task.controller.http

import java.util.UUID
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.task.api.TasksApiDelegate
import r3nny.codest.task.api.TasksApiResponses
import r3nny.codest.task.dto.http.CreateTaskRequestDto
import r3nny.codest.task.logic.CreateTaskOperation
import r3nny.codest.task.logic.GetTaskInternalOperation
import r3nny.codest.task.logic.GetTasksListFrontendOperation
import r3nny.codest.task.model.CreateTaskRequest
import r3nny.codest.task.model.Level
import ru.tinkoff.kora.common.Component

@Component
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val getTaskInternalOperation: GetTaskInternalOperation,
    private val getTasksListFrontendOperation: GetTasksListFrontendOperation,
): TasksApiDelegate {
    override suspend fun createTask(createTaskRequest: CreateTaskRequest): TasksApiResponses.CreateTaskApiResponse {
        createTaskOperation.activate(createTaskRequest.toDto())
    }

    override suspend fun getFullTask(taskId: UUID): TasksApiResponses.GetFullTaskApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskInternal(taskId: UUID): TasksApiResponses.GetTaskInternalApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getTaskLite(taskId: UUID): TasksApiResponses.GetTaskLiteApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksList(
        offset: Long,
        limit: Int,
        level: Level?
    ): TasksApiResponses.GetTasksListApiResponse {
        TODO("Not yet implemented")
    }
}

private fun CreateTaskRequest.toDto() = CreateTaskRequestDto(
    name = name,
    description = description,
    methodName = methodName,
    parameters = TaskParameters(
        inputTypes = ,
        outputType = outputType
    )
)
