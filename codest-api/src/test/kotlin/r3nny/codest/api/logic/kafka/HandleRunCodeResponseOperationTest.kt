package r3nny.codest.api.logic.kafka

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.dto.runner.CodeRunnerErrorType
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import java.time.LocalDateTime
import java.util.*

class HandleRunCodeResponseOperationTest : OperationTestBase() {
    private val stubAttemptId = UUID.randomUUID()
    private val event = RunCodeResponseEvent(
        errorType = null,
        output = emptyList()
    )
    private val operation = HandleRunCodeResponseOperation(attemptsAdapter, kafkaAdapter)
    private val stubAttempt = AttemptDto(
        id = stubAttemptId,
        status = StatusDto.PENDING,
        taskId = UUID.randomUUID(),
        error = null,
        code = "some code",
        language = Language.JAVA,
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `success flow`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt
        coEvery { attemptsAdapter.updateStatus(stubAttemptId, StatusDto.ACCEPTED) } returns true

        operation.activate(stubAttemptId, event)

        coVerify {
            attemptsAdapter.updateStatus(stubAttemptId, StatusDto.ACCEPTED)
            kafkaAdapter.sendCacheInvalidate(event = CacheInvalidateEvent(solutionId = stubAttemptId))
        }
    }

    @Test
    fun `success flow - with error`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt
        coEvery { attemptsAdapter.updateStatus(stubAttemptId, StatusDto.TEST_ERROR, listOf("error", "message")) } returns true

        operation.activate(stubAttemptId, event.copy(CodeRunnerErrorType.TEST_ERROR, listOf("error", "message")))

        coVerify {
            attemptsAdapter.updateStatus(stubAttemptId, StatusDto.TEST_ERROR, listOf("error", "message"))
            kafkaAdapter.sendCacheInvalidate(event = CacheInvalidateEvent(solutionId = stubAttemptId))
        }
    }

    @Test
    fun `success flow - already not pending on update`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt
        coEvery { attemptsAdapter.updateStatus(stubAttemptId, StatusDto.ACCEPTED) } returns false

        operation.activate(stubAttemptId, event)

        coVerify(exactly = 0) {
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `success flow - already not pending on get from cache`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt.copy(
            status = StatusDto.ACCEPTED
        )

        operation.activate(stubAttemptId, event)

        coVerify(exactly = 0) {
            attemptsAdapter.updateStatus(any(), any(), any())
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `error flow - invocation error`() = runBlocking {
        val ex = InvocationException(InvocationExceptionCode.ATTEMPTS_ERROR)
        coEvery { attemptsAdapter.getById(stubAttemptId) } throws ex
        coEvery {
            attemptsAdapter.updateStatus(
                id = stubAttemptId,
                status = StatusDto.INTERNAL_ERROR,
                error = listOf("Непредвиденная ошибка по id = $stubAttemptId")
            )
        } returns true

        val code = shouldThrow<InvocationException> {
            operation.activate(stubAttemptId, event)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.ATTEMPTS_ERROR

        coVerify {
            attemptsAdapter.updateStatus(
                id = stubAttemptId,
                status = StatusDto.INTERNAL_ERROR,
                error = listOf("Непредвиденная ошибка по id = $stubAttemptId")
            )
            kafkaAdapter.sendCacheInvalidate(event = CacheInvalidateEvent(solutionId = stubAttemptId))
        }
    }

}
