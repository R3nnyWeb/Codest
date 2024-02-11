package r3nny.codest.shared.dto.runner

data class RunCodeResponseEvent(
    val errorType: CoreRunnerErrorType?,
    val output: List<String>
)

enum class CoreRunnerErrorType {
    COMPILE_ERROR,
    RUNTIME_ERROR,
    INTERNAL_ERROR,
    TIME_EXCEED_ERROR
}