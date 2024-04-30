package r3nny.codest.task.logic

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.exception.LogicExceptionCode
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.integration.db.TestAdapter
import java.util.*

class GetFullTaskOperation(
    private val taskAdapter: TaskAdapter,
    private val testAdapter: TestAdapter
) {

    @LogMethod
    suspend fun activate(taskId: UUID): Pair<TaskDto, List<TestCase>> {
        val task = taskAdapter.getFullById(taskId) ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )
        val tests = testAdapter.getAllByTaskId(taskId)
        return Pair(task, tests)
    }

}
