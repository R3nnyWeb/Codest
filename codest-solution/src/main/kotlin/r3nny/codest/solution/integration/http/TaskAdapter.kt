package r3nny.codest.solution.integration.http

import r3nny.codest.shared.domain.Language
import r3nny.codest.solution.integration.http.task.model.InternalTaskResponse
import ru.tinkoff.kora.common.util.Either
import java.util.*

class TaskAdapter {

    suspend fun getTaskInternal(taskId: UUID, language: Language): Either<InternalTaskResponse, String> {
        throw Exception("Not yet implemented")
    }

    companion object {
        const val TASK_NOT_FOUND_ERROR = "TaskNotFound"
        const val LANGUAGE_NOT_ACCEPTABLE_ERROR = "InternalNotExist"
    }

}
