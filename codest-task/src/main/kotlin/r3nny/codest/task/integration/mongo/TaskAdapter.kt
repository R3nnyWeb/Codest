package r3nny.codest.task.integration.mongo

import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.dao.TaskDTO
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
    fun getAll(): List<TaskDTO> = repo.findAll()

    @LogMethod
    fun updateTaskEnabled(taskId: UUID, enabled: Boolean) {

    }
}
