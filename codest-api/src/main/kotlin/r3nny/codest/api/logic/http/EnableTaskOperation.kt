package r3nny.codest.api.logic.http

import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.shared.exception.throwSecurityException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class EnableTaskOperation(
    private val attemptsAdapter: AttemptsAdapter,
    private val taskAdapter: TaskAdapter,
    private val kafkaAdapter: KafkaAdapter,
) {

    suspend fun activate(taskId: UUID, userId: UUID, enable: Boolean) {
        val task = taskAdapter.getById(taskId) ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )
        if (task.userId != userId) {
            throwSecurityException(
                code = DefaultSecurityExceptionCode.FORBIDDEN,
                message = "Вы не можете редактировать эту задачу"
            )
        }

        if (!enable) {
            taskAdapter.updateEnable(taskId, false)
        } else {
            val notSuccessLanguages = task.languages.minus(attemptsAdapter.getSuccessAttemptLanguages(taskId))
            if (notSuccessLanguages.isNotEmpty()) {
                throwLogicException(
                    code = LogicExceptionCode.ENABLE_TASK_ERROR,
                    message = "Не выполнялись успешные решения на языках: ${notSuccessLanguages.joinToString()}"
                )
            } else {
                taskAdapter.updateEnable(taskId, true)
            }
        }
        kafkaAdapter.sendCacheInvalidate(CacheInvalidateEvent(taskId = taskId))
    }

}
