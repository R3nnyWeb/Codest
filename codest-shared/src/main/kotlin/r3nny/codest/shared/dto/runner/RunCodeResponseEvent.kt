package r3nny.codest.shared.dto.runner

import ru.tinkoff.kora.json.common.annotation.Json

@Json
data class RunCodeResponseEvent(
    val errorType: CodeRunnerErrorType?,
    val output: List<String>
)

@Json
enum class CodeRunnerErrorType {
    COMPILE_ERROR,
    RUNTIME_ERROR,
    INTERNAL_ERROR,
    TIME_EXCEED_ERROR
}