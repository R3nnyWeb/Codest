package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.task.dto.common.Level
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.exception.LogicExceptionCode
import java.util.*

class GetInternalTaskOperationTest : OperationTestBase() {
    private val operation = GetInternalTaskOperation(
        taskAdapter, testAdapter
    )
    private val taskDto = TaskDto(
        id = UUID.randomUUID(),
        name = "task name",
        methodName = "methodName",
        drivers = mapOf(
            Language.JAVA to "driver"
        ),
        startCode = mapOf(
            Language.JAVA to "startCode"
        ),
        languages = setOf(Language.JAVA),
        isEnabled = true,
        isPrivate = true,
        description = "description",
        level = Level.EASY,
        inputTypes = listOf(Type.INTEGER, Type.INTEGER),
        outputType = Type.INTEGER
    )
    private val tests = listOf(
        TestCase(
            id = UUID.randomUUID(), inputValues = listOf("0", "1"), outputValue = "1"
        )
    )
    private val stubTaskId = taskDto.id

    @Test
    fun `success flow`() = runBlocking {
        coEvery { taskAdapter.getFullById(taskId = stubTaskId) } returns taskDto
        coEvery { testAdapter.getAllByTaskId(taskId = stubTaskId) } returns tests

        val result = operation.activate(stubTaskId, Language.JAVA)

        result shouldBe Pair(taskDto, tests)
    }


    @Test
    fun `error flow - language not acceptable`() = runBlocking {
        coEvery { taskAdapter.getFullById(taskId = stubTaskId) } returns taskDto

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId, Language.PYTHON)
        }.exceptionCode

        code shouldBe LogicExceptionCode.INTERNAL_NOT_EXIST

        coVerify(exactly = 0) {
            testAdapter.getAllByTaskId(any())
        }
    }

    @Test
    fun `error flow - task not found`() = runBlocking {
        coEvery { taskAdapter.getFullById(taskId = stubTaskId) } returns null

        val code = shouldThrow<LogicException> {
            operation.activate(stubTaskId, Language.JAVA)
        }.exceptionCode

        code shouldBe LogicExceptionCode.TASK_NOT_FOUND

        coVerify(exactly = 0) {
            testAdapter.getAllByTaskId(any())
        }
    }

}