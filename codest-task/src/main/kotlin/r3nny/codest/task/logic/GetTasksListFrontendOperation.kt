package r3nny.codest.task.logic

import r3nny.codest.task.dto.http.TaskListFrontend
import r3nny.codest.task.integration.db.TaskAdapter
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class GetTasksListFrontendOperation(
    private val adapter: TaskAdapter
) {

    @Log
   open suspend fun activate(): List<TaskListFrontend> {
        return emptyList()
    }
}
