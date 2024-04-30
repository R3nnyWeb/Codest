package r3nny.codest.task.logic

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.exception.LogicExceptionCode
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.integration.db.TestAdapter
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class GetInternalTaskOperation(
    private val taskAdapter: TaskAdapter,
    private val testAdapter: TestAdapter,
) {

    @LogMethod
    open suspend fun activate(taskId: UUID, language: Language): Pair<TaskDto, List<TestCase>> {
        val task = taskAdapter.getFullById(taskId) ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )
        if(language !in task.languages) throwLogicException(
            code = LogicExceptionCode.INTERNAL_NOT_EXIST, message = "по id = $taskId и языку = $language"
        )
        val tests = testAdapter.getAllByTaskId(taskId)
        return Pair(task, tests)
    }
}

