package r3nny.codest.shared.dto.runner

import ru.tinkoff.kora.json.common.annotation.Json

@Json
data class RunCodeRequestEvent(
    val code : String,
    val input: List<String>?,
    val language: ExecutableLanguage
)

@Json
enum class ExecutableLanguage {
    JAVA_17,
    JAVA_8,
    PYTHON_3
}
