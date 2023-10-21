package r3nny.codest.executor.domain

data class TestOutput(
    val actual : String,
    val timeMs: Long,
    val memoryMb: Long
)
