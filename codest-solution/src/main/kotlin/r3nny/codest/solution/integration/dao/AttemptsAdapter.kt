package r3nny.codest.solution.integration.dao

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.wrap
import r3nny.codest.solution.dto.dao.AttemptDto
import r3nny.codest.solution.exception.InvocationExceptionCode
import java.util.*

class AttemptsAdapter(
    private val repository: AttemptsRepository,
) {

    suspend fun saveAttempt(
        taskId: UUID,
        userId: UUID,
        code: String,
        language: Language,
    ): AttemptDto {
        return wrap(errorCode = InvocationExceptionCode.ATTEMPTS) {
            repository.saveAttempt(
                id = UUID.randomUUID(),
                taskId = taskId,
                userId = userId,
                code = code,
                language = language
            )
        }
    }

}
