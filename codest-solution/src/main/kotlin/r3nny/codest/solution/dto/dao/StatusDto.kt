package r3nny.codest.solution.dto.dao

import r3nny.codest.solution.model.SolutionStatus

enum class StatusDto(val db: String, val api: SolutionStatus) {
    ACCEPTED("accepted", SolutionStatus.ACCEPTED),
    PENDING("pending", SolutionStatus.PENDING),
    RUNTIME_ERROR("runtime_error", SolutionStatus.RUNTIME_ERROR),
    COMPILE_ERROR("compile_error", SolutionStatus.COMPILE_ERROR),
    TEST_ERROR("test_error", SolutionStatus.TEST_ERROR),
    TIMEOUT_ERROR("timeout_error", SolutionStatus.TIMEOUT_ERROR),
    ;

    companion object {
        val dbMap = entries.associateBy { it.db }
        val apiMap = entries.associateBy { it.api }

        fun fromDb(db: String) = dbMap[db] ?: throw IllegalArgumentException("Unexpected value '$db'")
        fun fromApi(api: SolutionStatus) = apiMap[api] ?: throw IllegalArgumentException("Unexpected value '$api'")
    }
}