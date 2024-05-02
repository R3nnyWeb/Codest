package r3nny.codest.api.integration.db

import r3nny.codest.api.cache.GetAttemptCache
import r3nny.codest.api.dto.dao.AttemptByTaskDto
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.wrap
import ru.tinkoff.kora.cache.annotation.Cacheable
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class AttemptsAdapter(
    private val repository: AttemptsRepository,
) {

    suspend fun saveAttempt(
        taskId: UUID,
        userId: UUID,
        code: String,
        language: Language,
    ): AttemptDto {
        return wrap(errorCode = InvocationExceptionCode.ATTEMPTS_ERROR) {
            repository.saveAttempt(
                id = UUID.randomUUID(),
                taskId = taskId,
                userId = userId,
                code = code,
                language = language
            )
        }
    }

    suspend fun getAllByTaskIdAndUserId(taskId: UUID, userId: UUID): List<AttemptByTaskDto> {
        return wrap(errorCode = InvocationExceptionCode.ATTEMPTS_ERROR) {
            repository.getAllByTaskIdAndUserId(
                taskId, userId
            )
        }
    }

    @Cacheable(GetAttemptCache::class)
    open suspend fun getById(id: UUID): AttemptDto? {
        return wrap(errorCode = InvocationExceptionCode.ATTEMPTS_ERROR) {
            repository.getById(id)
        }
    }

    open suspend fun updateStatus(id: UUID, status: StatusDto, error: List<String>? = null): Boolean {
       return wrap(errorCode = InvocationExceptionCode.ATTEMPTS_ERROR) {
            repository.updateStatus(id, status, error).value != 0L
        }
    }

}
