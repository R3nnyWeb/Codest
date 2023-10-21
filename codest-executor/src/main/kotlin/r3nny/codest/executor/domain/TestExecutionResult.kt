package r3nny.codest.executor.domain

import r3nny.codest.shared.domain.TestCase

data class TestExecutionResult(
    val success: Boolean,
    val timeMs: Double,
    val actualResult: String,
    val memoryMb: Double,
    val test: TestCase
)
