package r3nny.codest.shared.dto

import r3nny.codest.shared.domain.TestCase
import java.util.UUID

data class TaskInternalDTO(
    val taskId: UUID,
    val driver: String,
    val tests: List<TestCase>,
)