package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.dao.AttemptByTaskDto
import r3nny.codest.api.integration.db.AttemptsAdapter
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class GetUserSolutionsForTaskOperation(
    private val attemptsAdapter: AttemptsAdapter
) {
    suspend fun activate(userId: UUID, taskId: UUID): List<AttemptByTaskDto>
    = attemptsAdapter.getAllByTaskIdAndUserId(taskId, userId)

}
