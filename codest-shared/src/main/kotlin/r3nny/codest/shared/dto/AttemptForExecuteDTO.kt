package r3nny.codest.shared.dto

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import java.util.UUID

data class AttemptForExecuteDTO(
    val id: UUID,
    val code: String,
    val language: Language,
    val methodName: String,
    val parameters: TaskParameters,
    val testCases: Set<TestCase>,
    val timeLimitMs: Long,
    val memoryLimitMb: Long,
)
