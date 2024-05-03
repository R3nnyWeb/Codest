package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.throwLogicException
import ru.tinkoff.kora.common.Component
import java.time.LocalDateTime
import java.util.*

@Component
class GetAttemptByIdOperation(
    private val attemptsAdapter: AttemptsAdapter,
    private val kafkaAdapter: KafkaAdapter,
) {

    suspend fun activate(id: UUID): AttemptDto {
        val attempt = attemptsAdapter.getById(id) ?: throwLogicException(
            code = LogicExceptionCode.ATTEMPT_NOT_FOUND, message = "по id =  $id"
        )

        if (attempt.status != StatusDto.PENDING || attempt.createdAt > LocalDateTime.now()
                .minusMinutes(1)
        ) return attempt

        return attempt.copy(
            status = StatusDto.INTERNAL_ERROR, error = listOf("Ошибка. id = $id")
        ).also { updated ->
            val isUpdated = attemptsAdapter.updateStatus(updated.id, updated.status, updated.error)
            if (isUpdated) {
                kafkaAdapter.sendCacheInvalidate(event = CacheInvalidateEvent(solutionId = updated.id))
            }
        }
    }
}
