package r3nny.codest.shared.dto.runner

import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.json.common.annotation.Json

@Json
data class RunCodeRequestEvent(
    val code : String,
    val tests: List<ExecutionTestCase>,
    val language: Language
)

@Json
data class ExecutionTestCase(
    val inputData: List<String>,
    val outputData: String
)
