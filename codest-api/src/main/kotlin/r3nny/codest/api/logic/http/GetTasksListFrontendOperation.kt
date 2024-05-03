package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.model.TaskLite
import r3nny.codest.model.TasksListResponse
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class GetTasksListFrontendOperation(
    private val adapter: TaskAdapter,
) {

    @Log
    open suspend fun activate(query: String?, level: Level?, offset: Long, limit: Int): TasksListResponse {
        val count = adapter.count() //todo: cache на страницы + level
        val list = adapter.getList(query, level, offset, limit)
        return TasksListResponse(
            currentPage = offset,
            totalPages = (count + limit - 1) / limit,
            items = list.map {
                TaskLite(it.id, it.name, r3nny.codest.model.Level.fromValue(it.level.name.lowercase()))
            }
        )
    }
}
