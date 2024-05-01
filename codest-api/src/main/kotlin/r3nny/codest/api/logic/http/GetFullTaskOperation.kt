package r3nny.codest.api.logic.http

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.api.integration.db.TestAdapter
import java.util.*

class GetFullTaskOperation(
    private val taskAdapter: TaskAdapter,
    private val testAdapter: TestAdapter
) {

    @LogMethod
    suspend fun activate(taskId: UUID): Pair<TaskDto, List<TestCase>> {
        val task = taskAdapter.getById(taskId) ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )
        val tests = testAdapter.getAllByTaskId(taskId)
        return Pair(task, tests)
    }

}
