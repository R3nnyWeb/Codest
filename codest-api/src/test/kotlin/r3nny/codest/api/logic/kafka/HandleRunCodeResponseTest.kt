package r3nny.codest.api.logic.kafka

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import java.util.*

class HandleRunCodeResponseTest : OperationTestBase() {
    private val stubAttemptId = UUID.randomUUID()
    private val event = RunCodeResponseEvent(
        errorType = null,
        output = emptyList()
    )
    private val operation = HandleRunCodeResponse(attemptsAdapter, kafkaAdapter)

    @Test
    fun `error flow - attempt error`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } throws InvocationException(InvocationExceptionCode.ATTEMPTS_ERROR)

        val code = shouldThrow<InvocationException> {
            operation.activate(stubAttemptId, event)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.ATTEMPTS_ERROR

        coVerify {
            attemptsAdapter.updateStatus(id = )
        }
    }


}