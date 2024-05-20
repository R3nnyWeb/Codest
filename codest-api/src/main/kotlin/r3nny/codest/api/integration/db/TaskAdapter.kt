package r3nny.codest.api.integration.db

import r3nny.codest.api.cache.GetTaskCache
import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.dto.http.TaskListFrontend
import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.wrap
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

    open suspend fun getList(query: String?, level: Level?, offset: Long, limit: Int): List<TaskListFrontend> {
        return wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) {
            taskRepository.getList(query, level, offset, limit)
        }
    }

    open suspend fun getUserIdByTaskId(taskId: UUID): UUID? {
        return wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) {
            taskRepository.getUserIdByTaskId(taskId)
        }
    }

    open suspend fun count() = wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) { taskRepository.count() }
    open suspend fun getAllByUserId(userId: UUID): List<TaskDto> {
        return wrap(errorCode = InvocationExceptionCode.GET_TASK_ERROR) {
            taskRepository.getAllByUserId(userId)
        }
    }

    open suspend fun updateEnable(taskId: UUID, enable: Boolean) {
        return wrap(errorCode = InvocationExceptionCode.ATTEMPTS_ERROR) {
            taskRepository.updateEnable(taskId, enable)
        }
    }
}
