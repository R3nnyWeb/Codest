package r3nny.codest.api.controller.http

import r3nny.codest.api.TasksApiDelegate
import r3nny.codest.api.TasksApiResponses
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.logic.http.CreateTaskOperation
import r3nny.codest.api.logic.http.EnableTaskOperation
import r3nny.codest.api.logic.http.GetFullTaskOperation
import r3nny.codest.api.logic.http.GetLiteTaskOperation
import r3nny.codest.api.logic.http.GetOwnTasksOperation
import r3nny.codest.api.logic.http.GetTasksListFrontendOperation
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.model.CreateTaskRequest
import r3nny.codest.model.CreateTaskResponse
import r3nny.codest.model.FullTaskResponse
import r3nny.codest.model.Level
import r3nny.codest.model.TasksListResponse
import r3nny.codest.model.Test
import r3nny.codest.shared.PrincipalImpl
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Principal
import java.util.*

@Component
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val getLiteTaskOperation: GetLiteTaskOperation,
    private val getFullTaskOperation: GetFullTaskOperation,
    private val getTasksListFrontendOperation: GetTasksListFrontendOperation,
    private val enableTaskOperation: EnableTaskOperation,
    private val getOwnTasksOperation: GetOwnTasksOperation,
) : TasksApiDelegate {

    @LogMethod
    override suspend fun createTask(createTaskRequest: CreateTaskRequest): TasksApiResponses.CreateTaskApiResponse {
        val userId = (Principal.current() as PrincipalImpl).userId
        val id = createTaskOperation.activate(createTaskRequest, userId)

        return TasksApiResponses.CreateTaskApiResponse.CreateTask200ApiResponse(
            content = CreateTaskResponse(id)
        )
    }

    override suspend fun enableTask(taskId: UUID, enable: Boolean): TasksApiResponses.EnableTaskApiResponse {
        enableTaskOperation.activate(taskId, (Principal.current() as PrincipalImpl).userId, enable)
        return TasksApiResponses.EnableTaskApiResponse.EnableTask200ApiResponse()
    }

    override suspend fun getOwnTasks(): TasksApiResponses.GetOwnTasksApiResponse {
        return TasksApiResponses.GetOwnTasksApiResponse.GetOwnTasks200ApiResponse(getOwnTasksOperation.activate((Principal.current() as PrincipalImpl).userId))
    }

    override suspend fun getFullTask(taskId: UUID): TasksApiResponses.GetFullTaskApiResponse {
        return getFullTaskOperation.activate(taskId).toResponse()
    }

    override suspend fun getTaskLite(taskId: UUID): TasksApiResponses.GetTaskLiteApiResponse {
        val userId = (Principal.current() as? PrincipalImpl)?.userId
        return TasksApiResponses.GetTaskLiteApiResponse.GetTaskLite200ApiResponse(
            getLiteTaskOperation.activate(
                taskId,
                userId
            ).toResponse()
        )
    }

    override suspend fun getTasksList(
        offset: Long?,
        limit: Int?,
        level: Level?,
        search: String?,
    ): TasksApiResponses.GetTasksListApiResponse {
        return getTasksListFrontendOperation.activate(
            search,
            level?.let { r3nny.codest.api.dto.common.Level.fromString(it.name) },
            offset ?: 0,
            limit ?: 10
        ).toResponse()
    }
}

private fun TasksListResponse.toResponse() = TasksApiResponses.GetTasksListApiResponse.GetTasksList200ApiResponse(
    content = this
)

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
            userId = first.userId,
            tests = second.map {
                Test(
                    id = it.id,
                    inputData = it.inputValues,
                    outputData = it.outputValue
                )
            }
        )
    )
