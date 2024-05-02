package r3nny.codest.api.logic.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.LogicException
import java.time.LocalDateTime
import java.util.*

class GetAttemptByIdOperationTest : OperationTestBase() {
    private val operation = GetAttemptByIdOperation(
        attemptsAdapter = attemptsAdapter,
        kafkaAdapter = kafkaAdapter,
    )
    private val stubAttemptId = UUID.randomUUID()
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
    fun `success flow - pending`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt

        val result = operation.activate(stubAttemptId)

        result shouldBe stubAttempt

        coVerify(exactly = 0) {
            attemptsAdapter.updateStatus(any(), any(), any())
            kafkaAdapter.sendCacheInvalidate(
                CacheInvalidateEvent(
                    solutionId = stubAttemptId
                )
            )
        }
    }

    @Test
    fun `success flow`() = runBlocking {
        val copied = stubAttempt.copy(
            status = StatusDto.ACCEPTED,
            createdAt = LocalDateTime.now().minusMinutes(10)
        )
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns copied

        val result = operation.activate(stubAttemptId)

        result shouldBe copied

        coVerify(exactly = 0) {
            attemptsAdapter.updateStatus(any(), any(), any())
            kafkaAdapter.sendCacheInvalidate(
                CacheInvalidateEvent(
                    solutionId = stubAttemptId
                )
            )
        }
    }

    @Test
    fun `success flow - internal error by timeout without update`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt.copy(
            createdAt = LocalDateTime.now().minusMinutes(2)
        )
        coEvery {
            attemptsAdapter.updateStatus(
                stubAttemptId,
                StatusDto.INTERNAL_ERROR,
                listOf("Ошибка. id = ${stubAttempt.id}")
            )
        } returns false

        val result = operation.activate(stubAttemptId)

        result.error shouldBe listOf("Ошибка. id = ${stubAttempt.id}")
        result.status shouldBe StatusDto.INTERNAL_ERROR

        coVerify(exactly = 0) {
            kafkaAdapter.sendCacheInvalidate(
                CacheInvalidateEvent(
                    solutionId = stubAttemptId
                )
            )
        }
    }

    @Test
    fun `success flow - internal error by timeout with update`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns stubAttempt.copy(
            createdAt = LocalDateTime.now().minusMinutes(2)
        )
        coEvery {
            attemptsAdapter.updateStatus(
                stubAttemptId,
                StatusDto.INTERNAL_ERROR,
                listOf("Ошибка. id = ${stubAttempt.id}")
            )
        } returns true

        val result = operation.activate(stubAttemptId)

        result.error shouldBe listOf("Ошибка. id = ${stubAttempt.id}")
        result.status shouldBe StatusDto.INTERNAL_ERROR

        coVerify {
            attemptsAdapter.updateStatus(stubAttemptId, StatusDto.INTERNAL_ERROR, result.error)
            kafkaAdapter.sendCacheInvalidate(
                CacheInvalidateEvent(
                    solutionId = stubAttemptId
                )
            )
        }
    }

    @Test
    fun `error flow - attempt not found`() = runBlocking {
        coEvery { attemptsAdapter.getById(stubAttemptId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubAttemptId)
        }.exceptionCode

        code shouldBe LogicExceptionCode.ATTEMPT_NOT_FOUND

        coVerify(exactly = 0) {
            attemptsAdapter.updateStatus(any(), any(), any())
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }
}
