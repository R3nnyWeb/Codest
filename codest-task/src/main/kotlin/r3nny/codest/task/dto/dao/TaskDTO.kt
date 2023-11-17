package r3nny.codest.task.dto.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.task.dto.http.TaskListFrontend
import java.util.UUID

@Document(collection = "tasks")
data class TaskDTO(
    @Id
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
