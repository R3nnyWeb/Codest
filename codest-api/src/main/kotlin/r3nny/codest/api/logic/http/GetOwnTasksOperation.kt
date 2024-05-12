package r3nny.codest.api.logic.http

import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.model.TaskLiteResponse
import java.util.*

class GetOwnTasksOperation(
    private val taskAdapter: TaskAdapter,
) {
    suspend fun activate(userId: UUID): List<TaskLiteResponse> =
        taskAdapter.getAllByUserId(userId).map { it.toLiteDto().toResponse() }
    
}
