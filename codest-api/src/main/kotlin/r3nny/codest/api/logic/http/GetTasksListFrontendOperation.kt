package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.http.TaskListFrontend
import r3nny.codest.api.integration.db.TaskAndTestAdapter
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class GetTasksListFrontendOperation(
    private val adapter: TaskAndTestAdapter
) {

    @Log
   open suspend fun activate(): List<TaskListFrontend> {
        return emptyList()
    }
}
