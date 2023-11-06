package r3nny.codest.task.dto.dao

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import java.util.UUID

data class TaskDTO(
    val id: UUID,
    val name: String,
    val enabled: Boolean = false,
    val drivers: Map<Language, String>,
    val description: String,
    val parameters: TaskParameters,
    val tests: List<TestCase>,
)
