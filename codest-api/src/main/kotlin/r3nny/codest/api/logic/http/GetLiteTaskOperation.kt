package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.common.TaskWithSolutionsDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.shared.exception.throwLogicException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class GetLiteTaskOperation(
    private val taskAdapter: TaskAdapter,
    private val attemptsAdapter: AttemptsAdapter
) {

    suspend fun activate(taskId: UUID, userId: UUID?): TaskWithSolutionsDto {
        val task = taskAdapter.getById(taskId)?.toLiteDto() ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )
        val solutions = userId?.let {
            attemptsAdapter.getAllByTaskIdAndUserId(taskId, it)
        } ?: emptyList()
        return TaskWithSolutionsDto(
            task = task,
            solutions = solutions
        )
    }
}