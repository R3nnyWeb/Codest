package r3nny.codest.shared.dto.runner

data class RunCodeRequestEvent(
    val code : String,
    val input: List<String>?,
    val language: ExecutableLanguage
)

enum class ExecutableLanguage {
    JAVA_17,
    JAVA_8,
    PYTHON_3
}
