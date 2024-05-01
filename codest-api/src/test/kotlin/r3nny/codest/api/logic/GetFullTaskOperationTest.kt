package r3nny.codest.api.logic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.http.GetFullTaskOperation
import java.util.*

class GetFullTaskOperationTest : OperationTestBase() {
    private val operation = GetFullTaskOperation(
        taskAdapter = taskAdapter,
        testAdapter = testAdapter
    )
    private val stubTaskId = UUID.randomUUID()

    @Test
    fun `error flow - task not found`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND

        coVerify(exactly = 0) {
            testAdapter.getAllByTaskId(stubTaskId)
        }
    }

}