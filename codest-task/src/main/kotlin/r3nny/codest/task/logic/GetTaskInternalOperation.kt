package r3nny.codest.task.logic

import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.TaskInternalDTO
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.integration.mongo.TaskAdapter
import java.util.UUID

@Service
class GetTaskInternalOperation(
    private val taskAdapter: TaskAdapter,
) {

    @LogMethod
    suspend fun activate(taskId: UUID, language: Language): TaskInternalDTO {
        val task = taskAdapter.getById(taskId) ?: throw Exception("Задача не найдена")

        return task.toInternal(language)
    }
}

private fun TaskDTO.toInternal(language: Language): TaskInternalDTO {
    return TaskInternalDTO(
        taskId = this.id,
        driver = this.drivers[language] ?: throw Exception("Driver not found for language $language"),
        tests = this.tests
    )
}
