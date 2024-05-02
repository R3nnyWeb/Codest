package r3nny.codest.runner.service

import r3nny.codest.shared.dto.runner.ExecutionTestCase
import ru.tinkoff.kora.common.Component

@Component
class ExecutionResultTester {

    fun findError(tests: List<ExecutionTestCase>, result: List<String>): Pair<String, String>? {
        require(tests.size == result.size)
        return tests.map { it.outputData }.zip(result).firstOrNull { it.first != it.second }
    }

}
