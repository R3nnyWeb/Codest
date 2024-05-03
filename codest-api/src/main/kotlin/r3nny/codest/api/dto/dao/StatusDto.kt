package r3nny.codest.api.dto.dao

import r3nny.codest.model.SolutionStatus
import r3nny.codest.shared.dto.runner.CodeRunnerErrorType

enum class StatusDto(val db: String, val api: SolutionStatus, val codeRunner: CodeRunnerErrorType?) {
    ACCEPTED("accepted", SolutionStatus.ACCEPTED, null),
    PENDING("pending", SolutionStatus.PENDING, null),
    RUNTIME_ERROR("runtime_error", SolutionStatus.RUNTIME_ERROR, CodeRunnerErrorType.RUNTIME_ERROR),
    COMPILE_ERROR("compile_error", SolutionStatus.COMPILE_ERROR, CodeRunnerErrorType.COMPILE_ERROR),
    TEST_ERROR("test_error", SolutionStatus.TEST_ERROR, CodeRunnerErrorType.TEST_ERROR),
    TIMEOUT_ERROR("timeout_error", SolutionStatus.TIMEOUT_ERROR, CodeRunnerErrorType.TIME_EXCEED_ERROR),
    INTERNAL_ERROR("internal_error", SolutionStatus.INTERNAL_ERROR, CodeRunnerErrorType.INTERNAL_ERROR),
    ;

    companion object {
        val dbMap = entries.associateBy { it.db }
        val apiMap = entries.associateBy { it.api }
        val codeErrorMap = entries.associateBy { it.codeRunner }

        fun fromDb(db: String) = dbMap[db] ?: throw IllegalArgumentException("Unexpected value '$db'")

        fun fromCodeRunner(codeRunner: CodeRunnerErrorType) = codeErrorMap[codeRunner]

        fun fromApi(api: SolutionStatus) = apiMap[api] ?: throw IllegalArgumentException("Unexpected value '$api'")
    }
}