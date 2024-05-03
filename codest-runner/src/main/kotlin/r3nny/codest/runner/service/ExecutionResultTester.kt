package r3nny.codest.runner.service

import r3nny.codest.shared.dto.runner.ExecutionTestCase
import ru.tinkoff.kora.common.Component

@Component
class ExecutionResultTester {

    fun findError(tests: List<ExecutionTestCase>, result: List<String>): Pair<ExecutionTestCase, String>? {
        require(tests.size == result.size)
        return tests.zip(result).firstOrNull { it.first.outputData != it.second }
    }

}
