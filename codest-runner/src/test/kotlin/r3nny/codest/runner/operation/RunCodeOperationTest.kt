package r3nny.codest.runner.operation

import io.kotest.common.runBlocking
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.ExecutionResult
import r3nny.codest.runner.service.ExecutionResultTester
import r3nny.codest.runner.service.executors.JavaCodeExecutor
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.CodeRunnerErrorType
import r3nny.codest.shared.dto.runner.ExecutionTestCase
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import ru.tinkoff.kora.application.graph.All
import java.util.*

class RunCodeOperationTest {
    private val id = UUID.randomUUID()
    private val event = RunCodeRequestEvent(
        code = "some code",
        tests = listOf(
            ExecutionTestCase(
                inputData = listOf("some"),
                outputData = "some output"
            )
        ),
        language = Language.JAVA
    )
    private val kafkaAdapter = mockk<KafkaClientAdapter>(relaxUnitFun = true)
    private val codeExecutor = mockk<JavaCodeExecutor>(relaxUnitFun = true) {
        every { this@mockk.languages } returns setOf(Language.JAVA)
    }
    private val tester: ExecutionResultTester = mockk()
    private val operation = RunCodeOperation(
        kafkaAdapter = kafkaAdapter,
        tester = tester,
        executors = All.of(codeExecutor)
    )

    @Test
    fun `success flow`() = runBlocking {
        coEvery { execute() } returns Pair(
            ExecutionResult(emptyList(), emptyList(), 0),
            ExecutionResult(listOf("some output"), emptyList(), 0)
        )
        coEvery { tester.findError(event.tests, listOf("some output")) } returns null

        operation.activate(event, id)

        coVerify {
            sendResponse(null, listOf("some output"))
        }
    }

        @Test
    fun `test error`() = runBlocking {
        coEvery { execute() } returns Pair(
            ExecutionResult(emptyList(), emptyList(), 0),
            ExecutionResult(listOf("some output"), emptyList(), 0)
        )
        coEvery { tester.findError(event.tests, listOf("some output")) } returns Pair("0", "some output")

        operation.activate(event, id)

        coVerify {
            sendResponse(CodeRunnerErrorType.TEST_ERROR, listOf("0", "some output"))
        }
    }


    @Test
    fun `runtime error`() = runBlocking {
        coEvery { execute() } returns Pair(
            ExecutionResult(emptyList(), emptyList(), 0),
            ExecutionResult(listOf("some output"), listOf("someError"), 1)
        )

        operation.activate(event, id)

        coVerify {
            sendResponse(CodeRunnerErrorType.RUNTIME_ERROR, listOf("some output", "someError"))
        }
    }

    @Test
    fun `compile error`() = runBlocking {
        coEvery { execute() } returns Pair(
            ExecutionResult(
                output = emptyList(),
                errorOutput = listOf("some", "errors"),
                exitCode = 1
            ),
            ExecutionResult(emptyList(), emptyList(), 1)
        )

        operation.activate(event, id)

        coVerify {
            sendResponse(CodeRunnerErrorType.COMPILE_ERROR, listOf("some", "errors"))
        }

    }

    @Test
    fun `timeout error`() = runBlocking {
        coEvery { execute() } throws InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)

        operation.activate(event, id)

        coVerify {
            sendResponse(CodeRunnerErrorType.TIME_EXCEED_ERROR, listOf("Время ожидания превышено"))
        }
    }

    @Test
    fun `internal error`() = runBlocking {
        coEvery { execute() } throws Exception("some error")

        operation.activate(event, id)

        coVerify {
            sendResponse(CodeRunnerErrorType.INTERNAL_ERROR, listOf("some error"))
        }
    }

    private suspend fun execute() = codeExecutor.execute(event)

    private suspend fun sendResponse(error: CodeRunnerErrorType?, output: List<String>) {
        kafkaAdapter.sendCodeRunResponse(
            id = id,
            response = RunCodeResponseEvent(
                errorType = error,
                output = output
            )
        )
    }


}
