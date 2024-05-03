package r3nny.codest.api.integration.db

import r3nny.codest.api.dto.dao.AttemptByTaskDto
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.database.common.UpdateCount
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

    @Query("SELECT id, status, created_at, language FROM attempts WHERE task_id = :taskId AND user_id = :userId")
    suspend fun getAllByTaskIdAndUserId(taskId: UUID, userId: UUID): List<AttemptByTaskDto>

    @Query("SELECT id, status, created_at, task_id, language, error, code FROM attempts WHERE id = :id")
    suspend fun getById(id: UUID): AttemptDto?


    @Query("UPDATE attempts SET status = :status, error = :error WHERE id = :id and status = 'pending'")
    suspend fun updateStatus(id: UUID, status: StatusDto, error: List<String>?): UpdateCount
}