package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.task.exception.LogicExceptionCode
import java.util.*

class GetLiteTaskOperationTest: OperationTestBase() {
    private val operation = GetLiteTaskOperation(
        taskAdapter
    )
    private val stubTaskId = UUID.randomUUID()

    @Test
    fun `error flow - task not found`() = runBlocking {
        coEvery { taskAdapter.getLiteById(stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND
    }

}
