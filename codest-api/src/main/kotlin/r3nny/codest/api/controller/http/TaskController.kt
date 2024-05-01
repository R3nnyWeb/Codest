package r3nny.codest.api.controller.http

import r3nny.codest.api.TasksApiDelegate
import r3nny.codest.api.TasksApiResponses
import r3nny.codest.api.dto.common.TaskLiteDto
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.logic.http.CreateTaskOperation
import r3nny.codest.api.logic.http.GetFullTaskOperation
import r3nny.codest.api.logic.http.GetLiteTaskOperation
import r3nny.codest.api.logic.http.GetTasksListFrontendOperation
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.model.CreateTaskRequest
import r3nny.codest.model.CreateTaskResponse
import r3nny.codest.model.FullTaskResponse
import r3nny.codest.model.Level
import r3nny.codest.model.TaskLiteResponse
import r3nny.codest.model.Test
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val getLiteTaskOperation: GetLiteTaskOperation,
    private val getFullTaskOperation: GetFullTaskOperation,
    private val getTasksListFrontendOperation: GetTasksListFrontendOperation,
) : TasksApiDelegate {

    @LogMethod
    override suspend fun createTask(createTaskRequest: CreateTaskRequest): TasksApiResponses.CreateTaskApiResponse {
        val id = createTaskOperation.activate(createTaskRequest)

        return TasksApiResponses.CreateTaskApiResponse.CreateTask200ApiResponse(
            content = CreateTaskResponse(id)
        )
    }

    override suspend fun getFullTask(taskId: UUID): TasksApiResponses.GetFullTaskApiResponse {
        return getFullTaskOperation.activate(taskId).toResponse()
    }

    @LogMethod
    override suspend fun getTaskLite(taskId: UUID, xUserId: UUID?): TasksApiResponses.GetTaskLiteApiResponse {
        return getLiteTaskOperation.activate(taskId, xUserId).toResponse()
    }

    override suspend fun getTasksList(
        offset: Long,
        limit: Int,
        level: r3nny.codest.model.Level?
    ): TasksApiResponses.GetTasksListApiResponse {
        TODO("Not yet implemented")
    }
}

private fun Pair<TaskDto, List<TestCase>>.toResponse() =
    TasksApiResponses.GetFullTaskApiResponse.GetFullTask200ApiResponse(
        content = FullTaskResponse(
            id = first.id,
            languages = first.languages.map { it.name.lowercase() },
            level = Level.valueOf(first.level.name.uppercase()),
            name = first.name,
            isEnabled = first.isEnabled,
            isPrivate = first.isPrivate,
            description = first.description,
            startCodes = first.startCode.mapKeys { (k, v) -> k.name.lowercase() },
            drivers = first.drivers.mapKeys { (k, v) -> k.name.lowercase() },
            inputTypes = first.inputTypes.map(Type::name),
            outputType = first.outputType.name,
            methodName = first.methodName,
            tests = second.map {
                Test(
                    id = it.id,
                    inputData = it.inputValues,
                    outputData = it.outputValue
                )
            }
        )
    )

private fun TaskLiteDto.toResponse() = TasksApiResponses.GetTaskLiteApiResponse.GetTaskLite200ApiResponse(
    content = TaskLiteResponse(
        id = id,
        languages = languages.map { it.name.lowercase() },
        level = Level.valueOf(level.name.uppercase()),
        name = name,
        isEnabled = isEnabled,
        isPrivate = isPrivate,
        description = description,
        startCodes = startCode.mapKeys { (k, v) -> k.name.lowercase() },
        solutions = emptyList()
    )
)
