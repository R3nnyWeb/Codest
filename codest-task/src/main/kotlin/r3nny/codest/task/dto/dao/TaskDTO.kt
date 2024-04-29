package r3nny.codest.task.dto.dao

import java.util.UUID
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.task.dto.http.TaskListFrontend

data class TaskDTO(
    val id: UUID,
    val name: String,
    val enabled: Boolean = false,
    val methodName: String,
    val drivers: Map<Language, String>,
    val startCode: Map<Language, String>,
    val description: String,
    val level: Level,
    val parameters: TaskParameters,
    val tests: List<TestCase>,
) {
    fun toFrontend(): TaskListFrontend = TaskListFrontend(
        id = id,
        name = name,
        level = level
    )
}
