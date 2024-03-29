package r3nny.codest.task.dto.http

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.task.dto.dao.Level

data class CreateTaskRequest(
    val name: String,
    val description: String,
    val methodName: String,
    val parameters: TaskParameters,
    val languages: Set<Language> = Language.values().toSet(),
    val level: Level,
    val startCode: Map<Language, String>,
    val tests: List<TestCase>,
)
