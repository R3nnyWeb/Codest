package r3nny.codest.task.integration.db

import java.util.UUID
import r3nny.codest.task.dto.dao.TaskDTO

class TaskAdapter {
   suspend fun getById(taskId: UUID): TaskDTO? {
        return null
    }

    fun updateLanguage(updatedTask: TaskDTO) {

    }

    fun createTask(task: TaskDTO) {

    }

    fun update(capture: TaskDTO) {

    }

}
