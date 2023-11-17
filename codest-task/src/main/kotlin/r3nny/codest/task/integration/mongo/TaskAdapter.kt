package r3nny.codest.task.integration.mongo

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.TaskListFrontend
import r3nny.codest.task.integration.mongo.criteria.TaskSearchQuery
import java.awt.print.Pageable
import java.util.UUID

@Service
class TaskAdapter(
    private val repo: TaskRepository
) {

    @LogMethod
    fun createTask(task: TaskDTO): UUID {
        return repo.save(task).id
    }

    @LogMethod
    fun getById(taskId: UUID): TaskDTO? {
        return repo.findById(taskId).orElse(null)
    }

    @LogMethod
    fun update(task: TaskDTO) {
        repo.save(task)
    }

    @LogMethod
    fun getListWithQuery(query: TaskSearchQuery, offset: Int, limit: Int): Page<TaskListFrontend> {
        return repo.findByLevelAndEnabledAndNameContaining(
            query.level,
            query.enabled,
            query.name,
            PageRequest.of(offset, limit) as Pageable
        ).map(TaskDTO::toFrontend)
    }

    @LogMethod
    fun updateTaskEnabled(taskId: UUID, enabled: Boolean) {

    }
}
