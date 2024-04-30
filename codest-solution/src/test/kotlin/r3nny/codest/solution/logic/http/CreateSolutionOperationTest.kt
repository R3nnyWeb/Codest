package r3nny.codest.solution.logic.http

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.InvocationException
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.solution.dto.dao.AttemptDto
import r3nny.codest.solution.dto.dao.StatusDto
import r3nny.codest.solution.exception.InvocationExceptionCode
import r3nny.codest.solution.exception.LogicExceptionCode
import r3nny.codest.solution.integration.dao.AttemptsAdapter
import r3nny.codest.solution.integration.http.TaskAdapter
import r3nny.codest.solution.integration.http.TaskAdapter.Companion.LANGUAGE_NOT_ACCEPTABLE_ERROR
import r3nny.codest.solution.integration.http.TaskAdapter.Companion.TASK_NOT_FOUND_ERROR
import r3nny.codest.solution.integration.http.task.model.InternalTaskResponse
import r3nny.codest.solution.integration.kafka.KafkaAdapter
import r3nny.codest.solution.model.CreateSolutionRequest
import ru.tinkoff.kora.common.util.Either
import java.time.LocalDateTime
import java.util.*

class CreateSolutionOperationTest {
    private val taskAdapter: TaskAdapter = mockk<TaskAdapter>()
    private val attemptsAdapter: AttemptsAdapter = mockk<AttemptsAdapter>(relaxUnitFun = true)
    private val kafkaAdapter: KafkaAdapter = mockk(relaxUnitFun = true)

    private val operation = CreateSolutionOperation(
        taskAdapter = taskAdapter,
        attemptsAdapter = attemptsAdapter,
        kafkaAdapter = kafkaAdapter
    )
    private val stubTaskId = UUID.randomUUID()
    private val stubUserId = UUID.randomUUID()
    private val stubLanguage = Language.JAVA
    private val stubRequest = CreateSolutionRequest(
        code = "code",
        language = stubLanguage.name
    )
    private val stubTaskDto = InternalTaskResponse(
        id = UUID.randomUUID(),
        isEnabled = true,
        isPrivate = true,
        tests = listOf(
            r3nny.codest.solution.integration.http.task.model.Test(
                id = UUID.randomUUID(),
                inputData = listOf("1", "0"),
                outputData = "1"
            ),
            r3nny.codest.solution.integration.http.task.model.Test(
                id = UUID.randomUUID(),
                inputData = listOf("2", "3"),
                outputData = "5"
            )
        ),
        driver = "some \${solution} driver"
    )
    private val stubAttempt = AttemptDto(
        id = UUID.randomUUID(),
        status = StatusDto.PENDING,
        code = "some code",
        error = "some error",
        language = stubLanguage,
        createdAt = LocalDateTime.now()
    )

    @Test
    fun `success flow`() = runBlocking {
        coEvery { taskAdapter.getTaskInternal(stubTaskId, stubLanguage) } returns Either.left(stubTaskDto)
        coEvery { attemptsAdapter.saveAttempt(stubTaskId, stubUserId, stubRequest.code, stubLanguage) } returns stubAttempt

        operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)

        coVerify {
            attemptsAdapter.saveAttempt(stubTaskId, stubUserId, stubRequest.code, stubLanguage)
                kafkaAdapter.sendCodeToExecute("some code driver", stubLanguage, listOf("1", "0", "2", "3"))
        }
    }

    @Test
    fun `error flow - not expected error`() = runBlocking {
        coEvery { taskAdapter.getTaskInternal(stubTaskId, stubLanguage) } returns Either.right("some")

        val code = shouldThrow<InvocationException> {
            operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.TASK_API
    }

    @Test
    fun `error flow - language not acceptable`() = runBlocking {
        coEvery { taskAdapter.getTaskInternal(stubTaskId, stubLanguage) } returns Either.right(
            LANGUAGE_NOT_ACCEPTABLE_ERROR
        )

        val code = shouldThrow<LogicException> {
            operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)
        }.exceptionCode

        code shouldBe LogicExceptionCode.LANGUAGE_NOT_ACCEPTABLE
    }

    @Test
    fun `error flow - task not found`() = runBlocking {
        coEvery { taskAdapter.getTaskInternal(stubTaskId, stubLanguage) } returns Either.right(TASK_NOT_FOUND_ERROR)

        val code = shouldThrow<LogicException> {
            operation.activate(taskId = stubTaskId, userId = stubUserId, request = stubRequest)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND
    }

}