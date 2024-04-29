package r3nny.codest.task.logic

import java.util.UUID
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.TaskInternalDTO
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.integration.db.TaskAdapter
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class GetTaskInternalOperation(
    private val taskAdapter: TaskAdapter,
) {

    @Log
    open suspend fun activate(taskId: UUID, language: Language): TaskInternalDTO {
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
