package r3nny.codest.solution.integration.dao

import r3nny.codest.shared.domain.Language
import r3nny.codest.solution.dto.dao.AttemptDto
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository
import java.util.*

@Repository
interface AttemptsRepository : JdbcRepository {

    @Query(
        """
        INSERT INTO attempts(id, task_id, user_id, code, language)
        VALUES(:id, :taskId, :userId, :code, :language)
        RETURNING id, task_id, user_id, code, language, error, created_at, status
    """
    )
    suspend fun saveAttempt(
        id: UUID,
        taskId: UUID,
        userId: UUID,
        code: String,
        language: Language,
    ): AttemptDto
}