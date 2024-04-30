package r3nny.codest.task.logic

import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.task.dto.dao.TaskLiteDto
import r3nny.codest.task.exception.LogicExceptionCode
import r3nny.codest.task.integration.db.TaskAdapter
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class GetLiteTaskOperation(
    private val taskAdapter: TaskAdapter,
) {

    suspend fun activate(taskId: UUID): TaskLiteDto = taskAdapter.getLiteById(taskId) ?: throwLogicException(
        code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
    )

}