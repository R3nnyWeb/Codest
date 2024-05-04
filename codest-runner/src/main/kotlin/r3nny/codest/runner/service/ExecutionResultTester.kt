package r3nny.codest.runner.service

import r3nny.codest.shared.dto.runner.ExecutionTestCase
import ru.tinkoff.kora.common.Component

@Component
class ExecutionResultTester {

   private fun <T> List<T>.getElementsWithEqualGaps(n: Int): List<T> {
    val result = mutableListOf<T>()
    val interval = this.size / n
    var index = interval - 1

    repeat(n) {
        if (index < this.size) {
            result.add(this[index])
            index += interval
        }
    }

    return result
}
    fun findError(tests: List<ExecutionTestCase>, result: List<String>): Pair<ExecutionTestCase, String>? {
        val onlyTestOutput = result.getElementsWithEqualGaps(tests.size)
        require(tests.size == onlyTestOutput.size)
        return tests.zip(onlyTestOutput).firstOrNull { it.first.outputData != it.second }
    }

}
