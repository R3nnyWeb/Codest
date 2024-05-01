package r3nny.codest.api.logic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.jupiter.api.Test
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.dto.dao.StatusDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.logic.http.CreateSolutionOperation
import r3nny.codest.model.CreateSolutionRequest
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.LogicException
import java.time.LocalDateTime
import java.util.*

class CreateSolutionOperationTest : OperationTestBase() {
    private val operation = CreateSolutionOperation(
        taskAdapter = taskAdapter,
        attemptsAdapter = attemptsAdapter,
        kafkaAdapter = kafkaAdapter,
        getAttemptCache = getAttemptCache,
        testAdapter = testAdapter
    )
    private val stubTaskId = UUID.randomUUID()
    private val stubUserId = UUID.randomUUID()
    private val stubLanguage = Language.JAVA
    private val stubRequest = CreateSolutionRequest(
        code = "code",
        language = stubLanguage.name
    )
    private val stubAttempt = AttemptDto(
        id = UUID.randomUUID(),
        status = StatusDto.PENDING,
        taskId = stubTaskId,
        userId = stubUserId,
        code = "some code",
        error = "some error",
        language = stubLanguage,
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `success flow`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved
        coEvery { testAdapter.getAllByTaskId(stubTaskId) } returns stubTests
        coEvery {
            attemptsAdapter.saveAttempt(
                stubTaskId,
                stubUserId,
                stubRequest.code,
                stubLanguage
            )
        } returns stubAttempt
        coEvery { getAttemptCache.put(stubAttempt.id, stubAttempt) } returns stubAttempt

        operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)

        coVerify {
            attemptsAdapter.saveAttempt(stubTaskId, stubUserId, stubRequest.code, stubLanguage)
            kafkaAdapter.sendCodeToExecute(stubAttempt.id, "driver code java", stubLanguage, listOf("2", "2", "2", "2"))
            getAttemptCache.put(stubAttempt.id, stubAttempt)
        }
    }

    @Test
    fun `error flow - language not acceptable`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns saved.copy(
            languages = setOf(Language.PYTHON)
        )

        val code = shouldThrow<LogicException> {
            operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)
        }.exceptionCode

        code shouldBe LogicExceptionCode.LANGUAGE_NOT_ACCEPTABLE
    }

    @Test
    fun `error flow - task not found`() = runBlocking {
        coEvery { taskAdapter.getById(stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND
    }

}