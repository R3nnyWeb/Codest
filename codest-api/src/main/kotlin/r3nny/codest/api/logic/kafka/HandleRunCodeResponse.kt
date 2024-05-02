package r3nny.codest.api.logic.kafka

import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.throwLogicException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class HandleRunCodeResponse(
    private val attemptsAdapter: AttemptsAdapter,
    private val kafkaAdapter: KafkaAdapter,
) {

    suspend fun activate(attemptId: UUID, response: RunCodeResponseEvent) {
        val attempt = attemptsAdapter.getById(attemptId) ?: throwLogicException(
            code = LogicExceptionCode.ATTEMPT_NOT_FOUND, message = "по id = $attemptId"
        )
        if (attempt.status != StatusDto.PENDING) return

        val wasUpdate = if (response.errorType == null) {
            attemptsAdapter.updateStatus(attemptId, StatusDto.ACCEPTED)
        } else {
            attemptsAdapter.updateStatus(attemptId, StatusDto.fromCodeRunner(response.errorType!!)!!, response.output)
        }

        if (wasUpdate) {
            kafkaAdapter.sendCacheInvalidate(
                event = CacheInvalidateEvent(solutionId = attemptId)
            )
        }
    }

}

