package r3nny.codest.solution.integration.http

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.wrap
import r3nny.codest.solution.exception.InvocationExceptionCode
import r3nny.codest.solution.integration.http.task.api.TasksApi
import r3nny.codest.solution.integration.http.task.api.TasksApiResponses
import r3nny.codest.solution.integration.http.task.model.InternalTaskResponse
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.util.Either
import java.util.*

@Component
class TaskAdapter(
    private val tasksApi: TasksApi,
) {

    suspend fun getTaskInternal(taskId: UUID, language: Language): Either<InternalTaskResponse, String> {
        return wrap(errorCode = InvocationExceptionCode.TASK_API) {
            when (val response = tasksApi.getTaskInternal(taskId, language.name)) {
                is TasksApiResponses.GetTaskInternalApiResponse.GetTaskInternal200ApiResponse -> Either.left(response.content)
                is TasksApiResponses.GetTaskInternalApiResponse.GetTaskInternal422ApiResponse -> Either.right(response.content.errorCode)
                else -> throw RuntimeException(response.toString())
            }
        }
    }

    companion object {
        const val TASK_NOT_FOUND_ERROR = "TaskNotFound"
        const val LANGUAGE_NOT_ACCEPTABLE_ERROR = "InternalNotExist"
    }

}
