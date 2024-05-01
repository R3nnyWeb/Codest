package r3nny.codest.api.integration.db

import r3nny.codest.shared.exception.wrap
import r3nny.codest.api.cache.GetTaskCache
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.exception.InvocationExceptionCode
import ru.tinkoff.kora.cache.annotation.Cacheable
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class TaskAdapter(
    private val taskRepository: TaskRepository,
) {

    @Cacheable(GetTaskCache::class)
    open suspend fun getById(taskId: UUID): TaskDto? {
        return wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) {
            taskRepository.getFullById(taskId)
        }
    }
}
