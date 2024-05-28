package r3nny.codest.api.logic.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.dto.dao.AttemptByTaskDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.OperationTestBase
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.LogicException
import java.time.LocalDateTime
import java.util.*

class GetLiteTaskOperationTest : OperationTestBase() {
    private val operation = GetLiteTaskOperation(
        taskAdapter,
    )
    private val stubTaskId = UUID.randomUUID()
    private val stubUserId = UUID.randomUUID()
    private val attempts = listOf(
        AttemptByTaskDto(
            id = UUID.randomUUID(),
            status = StatusDto.ACCEPTED,
            createdAt = LocalDateTime.now(),
            language = Language.PYTHON
        )
    )

    @Test
    fun `success flow`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved
        coEvery { attemptsAdapter.getAllByTaskIdAndUserId(taskId = stubTaskId, userId = stubUserId) } returns attempts

        val result = operation.activate(taskId = stubTaskId, userId = stubUserId)

        result.isAuthor shouldBe false
        result.task shouldBe saved.toLiteDto()
    }

    @Test
    fun `success flow - user is author`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved
        coEvery { attemptsAdapter.getAllByTaskIdAndUserId(taskId = stubTaskId, userId = saved.userId) } returns attempts

        val result = operation.activate(taskId = stubTaskId, userId = saved.userId)

        result.isAuthor shouldBe true
        result.task shouldBe saved.toLiteDto()
    }

    @Test
    fun `success flow - without user`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved

        val result = operation.activate(taskId = stubTaskId, userId = null)

        result.isAuthor shouldBe false
        result.task shouldBe saved.toLiteDto()

        coVerify(inverse = true) {
            attemptsAdapter.getAllByTaskIdAndUserId(any(), any())
        }
    }

    @Test
    fun `error flow - task not found`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId, null)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND
    }

}
