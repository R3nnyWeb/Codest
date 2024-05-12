package r3nny.codest.user.api.service

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import r3nny.codest.runner.service.ExecutionResultTester
import r3nny.codest.shared.dto.runner.ExecutionTestCase

class ExecutionResultTesterTest {
    private val tester = ExecutionResultTester()

    @Test
    fun `all correct`() {
        val output = listOf("2", "2", "0")
        val result = tester.findError(tests, output)
        result shouldBe null
    }


    @Test
    fun `last not correct`() {
        val output = listOf("2", "2", "3")
        val result = tester.findError(tests, output)
        result shouldBe Pair(
            ExecutionTestCase(
                inputData = listOf("-1", "1"),
                outputData = "0"
            ), "3"
        )
    }

    @Test
    fun `first not correct`() {
        val output = listOf("1", "2", "3")
        val result = tester.findError(tests, output)
        result shouldBe Pair(
            ExecutionTestCase(
                inputData = listOf("0", "2"),
                outputData = "2"
            ), "1"
        )
    }

    companion object {
        val tests = listOf(
            ExecutionTestCase(
                inputData = listOf("0", "2"),
                outputData = "2"
            ),
            ExecutionTestCase(
                inputData = listOf("1", "1"),
                outputData = "2"
            ),
            ExecutionTestCase(
                inputData = listOf("-1", "1"),
                outputData = "0"
            ),
        )
    }

}