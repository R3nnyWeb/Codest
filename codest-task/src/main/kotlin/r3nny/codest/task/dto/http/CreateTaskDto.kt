package r3nny.codest.task.dto.http

import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase

data class CreateTaskDto(
    val name: String,
    val description: String,
    val methodName: String,
    val parameters: TaskParameters,
    val codeHeader: String?,
    val tests: List<TestCase>,
)
