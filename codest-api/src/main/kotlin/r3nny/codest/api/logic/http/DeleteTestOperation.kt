package r3nny.codest.api.logic.http

import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.api.integration.db.TestAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.shared.exception.throwSecurityException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class DeleteTestOperation(
    private val testAdapter: TestAdapter,
    private val taskAdapter: TaskAdapter,
    private val kafkaAdapter: KafkaAdapter,
) {

    suspend fun activate(testId: UUID, userId: UUID) {
        val taskId = testAdapter.getTaskIdByTestId(testId) ?: throwLogicException(
            code = LogicExceptionCode.TEST_NOT_FOUND, message = "по id = $testId"
        )
        taskAdapter.getUserIdByTaskId(taskId)?.let {
            if (it != userId) {
                throwSecurityException(
                    code = DefaultSecurityExceptionCode.FORBIDDEN,
                    message = "Вы не можете редактировать эту задачу"
                )
            }
        } ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )

        testAdapter.delete(testId)
        kafkaAdapter.sendCacheInvalidate(
            event = CacheInvalidateEvent(
                taskId = taskId
            )
        )
    }
}