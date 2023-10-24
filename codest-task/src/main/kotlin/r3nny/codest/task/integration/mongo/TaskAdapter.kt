package r3nny.codest.task.integration.mongo

import r3nny.codest.task.dto.dao.TaskDTO
import java.util.UUID

class TaskAdapter {
    fun createTask(task : TaskDTO) : UUID {
        return UUID.randomUUID()
    }
}
