package r3nny.codest.task.integration.db

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.exception.wrap
import r3nny.codest.task.cache.GetFullTaskCache
import r3nny.codest.task.cache.GetLiteTaskCache
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.dao.TaskLiteDto
import r3nny.codest.task.exception.InvocationExceptionCode
import ru.tinkoff.kora.cache.annotation.Cacheable
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class TaskAdapter(
    private val taskRepository: TaskRepository,
) {

    @Cacheable(GetLiteTaskCache::class)
    @LogMethod
    open suspend fun getLiteById(taskId: UUID): TaskLiteDto? {
        return wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) {
            taskRepository.getLiteById(taskId)
        }
    }

    @Cacheable(GetFullTaskCache::class)
    @LogMethod
    open suspend fun getFullById(taskId: UUID): TaskDto? {
        return wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) {
            taskRepository.getFullById(taskId)
        }
    }
}
