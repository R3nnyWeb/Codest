package r3nny.codest.task.dto.dao

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import java.util.UUID

@Document(collection = "tasks")
data class TaskDTO(
    @Id
    val id: UUID,
    val name: String,
    val enabled: Boolean = false,
    val drivers: Map<Language, String>,
    val startCode: Map<Language, String>,
    val description: String,
    val parameters: TaskParameters,
    val tests: List<TestCase>,
)
