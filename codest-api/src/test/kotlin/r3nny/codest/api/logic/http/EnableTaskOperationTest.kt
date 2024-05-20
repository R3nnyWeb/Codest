package r3nny.codest.api.logic.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.shared.exception.SecurityException
import java.util.*

class EnableTaskOperationTest : OperationTestBase() {
    private val operation = EnableTaskOperation(
        attemptsAdapter = attemptsAdapter,
        taskAdapter = taskAdapter,
        kafkaAdapter = kafkaAdapter
    )
    private val stubTaskId = saved.id
    private val stubUserId = saved.userId

    @Test
    fun `success - enable`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved
        coEvery { attemptsAdapter.getSuccessAttemptLanguages(stubTaskId) } returns saved.languages

        operation.activate(stubTaskId, stubUserId, true)

        coVerify(exactly = 1) {
            taskAdapter.updateEnable(stubTaskId, true)
            kafkaAdapter.sendCacheInvalidate(
                CacheInvalidateEvent(
                    taskId = stubTaskId,
                )
            )
        }
    }

    @Test
    fun `error - cannot enable`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved
        coEvery { attemptsAdapter.getSuccessAttemptLanguages(stubTaskId) } returns setOf(Language.PYTHON)

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId, stubUserId, true)
        }.exceptionCode

        code shouldBe LogicExceptionCode.ENABLE_TASK_ERROR

        coVerify(exactly = 0) {
            taskAdapter.updateEnable(any(), any())
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }


    @Test
    fun `success - disable`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved

        operation.activate(stubTaskId, stubUserId, false)

        coVerify(exactly = 1) {
            taskAdapter.updateEnable(stubTaskId, false)
            kafkaAdapter.sendCacheInvalidate(
                CacheInvalidateEvent(
                    taskId = stubTaskId,
                )
            )
        }
    }

    @Test
    fun `error - not own`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved

        val code = shouldThrow<SecurityException> {
            operation.activate(stubTaskId, UUID.randomUUID(), false)
        }.exceptionCode

        code shouldBe DefaultSecurityExceptionCode.FORBIDDEN

        coVerify(exactly = 0) {
            taskAdapter.updateEnable(any(), any())
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

}
