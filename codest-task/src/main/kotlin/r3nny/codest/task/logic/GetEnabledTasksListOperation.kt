package r3nny.codest.task.logic

import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.http.TaskListFrontend
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.integration.mongo.criteria.TaskSearchQuery

@Service
class GetEnabledTasksListOperation(
    private val adapter: TaskAdapter
) {

    @LogMethod
    suspend fun activate(query: TaskSearchQuery, offset: Int, limit: Int): Page<TaskListFrontend> {
        return adapter.getListWithQuery(query, offset, limit)
    }
}
