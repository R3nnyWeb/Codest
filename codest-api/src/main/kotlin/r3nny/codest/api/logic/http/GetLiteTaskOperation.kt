package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.common.TaskLiteReponseDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.shared.exception.throwLogicException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class GetLiteTaskOperation(
    private val taskAdapter: TaskAdapter,
) {

    suspend fun activate(taskId: UUID, userId: UUID?): TaskLiteReponseDto {
        val task = taskAdapter.getById(taskId) ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )

        val isAuthor = task.userId == userId
        return TaskLiteReponseDto(
            task = task.toLiteDto(),
            isAuthor = isAuthor,
        )
    }
}