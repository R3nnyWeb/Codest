package r3nny.codest.task.controller.http

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.api.TasksApiResponses
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.dao.TaskLiteDto
import r3nny.codest.task.logic.CreateTaskOperation
import r3nny.codest.task.logic.GetFullTaskOperation
import r3nny.codest.task.logic.GetInternalTaskOperation
import r3nny.codest.task.logic.GetLiteTaskOperation
import r3nny.codest.task.logic.GetTasksListFrontendOperation
import r3nny.codest.task.model.CreateTaskRequest
import r3nny.codest.task.model.CreateTaskResponse
import r3nny.codest.task.model.FullTaskResponse
import r3nny.codest.task.model.InternalTaskResponse
import r3nny.codest.task.model.Level
import r3nny.codest.task.model.TaskLiteResponse
import r3nny.codest.task.model.Test
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val getLiteTaskOperation: GetLiteTaskOperation,
    private val getFullTaskOperation: GetFullTaskOperation,
    private val getInternalTaskOperation: GetInternalTaskOperation,
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

    override suspend fun getTaskInternal(taskId: UUID, language: String): TasksApiResponses.GetTaskInternalApiResponse {
        val language = Language.fromString(language)
        return getInternalTaskOperation.activate(taskId, language).toInternalResponse(language)
    }

    @LogMethod
    override suspend fun getTaskLite(taskId: UUID): TasksApiResponses.GetTaskLiteApiResponse {
        return getLiteTaskOperation.activate(taskId).toResponse()
    }

    override suspend fun getTasksList(
        offset: Long,
        limit: Int,
        level: Level?,
    ): TasksApiResponses.GetTasksListApiResponse {
        TODO("Not yet implemented")
    }
}

private fun Pair<TaskDto, List<TestCase>>.toInternalResponse(language: Language) =
    TasksApiResponses.GetTaskInternalApiResponse.GetTaskInternal200ApiResponse(
        content = InternalTaskResponse(
            id = first.id,
            isEnabled = first.isEnabled,
            isPrivate = first.isPrivate,
            driver = first.drivers[language] ?: error("Driver not found for language = $language"),
            tests = second.map {
                Test(
                    id = it.id,
                    inputData = it.inputValues,
                    outputData = it.outputValue
                )
            }
        )
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
        startCodes = startCode.mapKeys { (k, v) -> k.name.lowercase() }
    )
)
