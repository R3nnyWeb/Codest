package r3nny.codest.api.logic.kafka

import org.slf4j.LoggerFactory
import r3nny.codest.api.cache.GetAttemptCache
import r3nny.codest.api.cache.GetTaskCache
import r3nny.codest.api.cache.TestsByTaskIdCache
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import ru.tinkoff.kora.common.Component

@Component
class CacheInvalidateOperation(
    private val getAttemptCache: GetAttemptCache,
    private val getTaskCache: GetTaskCache,
    private val getTestsByTaskIdCache: TestsByTaskIdCache,
) {
    private val logger = LoggerFactory.getLogger(CacheInvalidateOperation::class.java)

    fun activate(event: CacheInvalidateEvent) {
        logger.info("Cache invalidate event: $event")

        if (event.taskId != null) {
            getTaskCache.invalidate(event.taskId)
            getTestsByTaskIdCache.invalidate(event.taskId)
        }
        if (event.solutionId != null) {
            getAttemptCache.invalidate(event.solutionId)
        }
    }
}