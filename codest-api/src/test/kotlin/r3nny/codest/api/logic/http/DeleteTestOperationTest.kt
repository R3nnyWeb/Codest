package r3nny.codest.api.logic.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.shared.exception.SecurityException
import java.util.*

class DeleteTestOperationTest : OperationTestBase() {
    private val operation = DeleteTestOperation(
        testAdapter, taskAdapter, kafkaAdapter
    )
    private val stubTaskId = saved.id
    private val stubUserId = saved.userId
    private val stubTestId = UUID.randomUUID()

    @Test
    fun `success flow`() = runBlocking {
        coEvery { testAdapter.getTaskIdByTestId(stubTestId) } returns stubTaskId
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns stubUserId

        operation.activate(stubTestId, stubUserId)

        coVerify(exactly = 1) {
            testAdapter.delete(stubTestId)
            kafkaAdapter.sendCacheInvalidate(
                event = CacheInvalidateEvent(
                    taskId = stubTaskId
                )
            )
        }
    }

    @Test
    fun `error flow - not owner`() = runBlocking {
        coEvery { testAdapter.getTaskIdByTestId(stubTestId) } returns stubTaskId
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns stubUserId

        val code = shouldThrow<SecurityException> {
            operation.activate(stubTestId, UUID.randomUUID())
        }.exceptionCode

        code shouldBe DefaultSecurityExceptionCode.FORBIDDEN

        coVerify(exactly = 0) {
            testAdapter.delete(stubTestId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `error - task not found`() = runBlocking {
        coEvery { testAdapter.getTaskIdByTestId(stubTestId) } returns stubTaskId
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTestId, stubUserId)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND

        coVerify(exactly = 0) {
            testAdapter.delete(stubTestId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `error - test not found`() = runBlocking {
        coEvery { testAdapter.getTaskIdByTestId(stubTestId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTestId, stubUserId)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TEST_NOT_FOUND

        coVerify(exactly = 0) {
            testAdapter.delete(stubTestId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

}