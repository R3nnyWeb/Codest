package r3nny.codest.api.logic.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.model.CreateTest
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.shared.exception.SecurityException
import java.util.*

class CreateTestsOperationTest : OperationTestBase() {
    private val operation = CreateTestsOperation(
        testAdapter, taskAdapter, kafkaAdapter
    )
    private val stubTaskId = saved.id
    private val stubUserId = saved.userId
    private val newTests = listOf(
        CreateTest(
            inputData = listOf(
                "2", "3"
            ),
            outputData = "5"
        ),
        CreateTest(
            inputData = listOf(
                "5", "3"
            ),
            outputData = "8"
        ),
        CreateTest(
            inputData = listOf(
                "5", "3"
            ),
            outputData = "8"
        ),
        CreateTest(
            inputData = listOf(
                "2", "0"
            ),
            outputData = "2"
        )
    )
    private val existingTests = listOf(
        TestCase(
            id = UUID.randomUUID(),
            inputValues = listOf(
                "2", "0"
            ),
            outputValue = "2"
        ),
        TestCase(
            id = UUID.randomUUID(),
            inputValues = listOf(
                "1", "0"
            ),
            outputValue = "1"
        )
    )

    @Test
    fun `success flow`() = runBlocking {
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns stubUserId
        coEvery { testAdapter.getAllByTaskId(stubTaskId) } returns existingTests

        val result = operation.activate(stubTaskId, stubUserId, newTests)

        coVerify(exactly = 1) {
            testAdapter.insert(result, stubTaskId)
            kafkaAdapter.sendCacheInvalidate(CacheInvalidateEvent(taskId = stubTaskId))
        }

        result.map {
            CreateTest(
                it.inputValues,
                it.outputValue
            )
        } shouldContainOnly listOf(
            CreateTest(
                inputData = listOf(
                    "2", "3"
                ),
                outputData = "5"
            ),
            CreateTest(
                inputData = listOf(
                    "5", "3"
                ),
                outputData = "8"
            ),
        )
    }

    @Test
    fun `error flow - same input - different output`() = runBlocking {
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns stubUserId
        coEvery { testAdapter.getAllByTaskId(stubTaskId) } returns existingTests

        val code = shouldThrow<LogicException> {
            val tests = newTests + CreateTest(
                inputData = listOf(
                    "5", "3"
                ),
                outputData = "4"
            )
            operation.activate(stubTaskId, stubUserId, tests)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TEST_NOT_CORRECT

        coVerify(exactly = 0) {
            testAdapter.insert(any(), stubTaskId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `error - input size not equals`() = runBlocking {
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns stubUserId
        coEvery { testAdapter.getAllByTaskId(stubTaskId) } returns existingTests

        val code = shouldThrow<LogicException> {
            val tests = newTests + CreateTest(
                inputData = listOf(
                    "5", "3", "4"
                ),
                outputData = "8"
            )
            operation.activate(stubTaskId, stubUserId, tests)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TEST_NOT_CORRECT

        coVerify(exactly = 0) {
            testAdapter.insert(any(), stubTaskId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `error - not owner`() = runBlocking {
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns UUID.randomUUID()

        val code = shouldThrow<SecurityException> {
            operation.activate(stubTaskId, stubUserId, newTests)
        }.exceptionCode

        code shouldBe DefaultSecurityExceptionCode.FORBIDDEN

        coVerify(exactly = 0) {
            testAdapter.insert(any(), stubTaskId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }

    @Test
    fun `error - task not found`() = runBlocking {
        coEvery { taskAdapter.getUserIdByTaskId(stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId, stubUserId, newTests)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND

        coVerify(exactly = 0) {
            testAdapter.insert(any(), stubTaskId)
            kafkaAdapter.sendCacheInvalidate(any())
        }
    }
}