package r3nny.codest.solution.integration.dao

import r3nny.codest.shared.domain.Language
import r3nny.codest.solution.dto.dao.AttemptDto
import java.util.*

class AttemptsAdapter {

    suspend fun saveAttempt(
        taskId: UUID,
        userId: UUID,
        code: String,
        language: Language,
    ): AttemptDto {
        throw NotImplementedError()
    }

}
