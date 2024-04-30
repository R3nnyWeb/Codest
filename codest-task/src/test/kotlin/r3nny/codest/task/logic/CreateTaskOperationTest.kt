package r3nny.codest.task.logic

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.task.dto.common.Level
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.extentions.languages
import r3nny.codest.task.dto.extentions.parameters
import r3nny.codest.task.dto.extentions.tests


class CreateTaskOperationTest : OperationTestBase() {
    private val operation = CreateTaskOperation(taskAndTestAdapter, driverGenerator)

    @Test
    fun success() = runBlocking {
        with(createTaskRequest) {
            val savedTask = slot<TaskDto>()
            val savedTests = slot<List<TestCase>>()
            coEvery {
                driverGenerator.generate(
                    createTaskRequest.methodName,
                    createTaskRequest.parameters(),
                    createTaskRequest.languages()
                )
            } returns mapOf(
                Language.JAVA to "driver java",
                Language.PYTHON to "driver python"
            )

            operation.activate(this)

            coVerify {
                taskAndTestAdapter.createTask(capture(savedTask), capture(savedTests))
            }

            with(savedTask.captured) {
                name shouldBe createTaskRequest.name
                drivers shouldBe mapOf(
                    Language.JAVA to "driver java",
                    Language.PYTHON to "driver python"
                )
                isEnabled shouldBe false
                description shouldBe createTaskRequest.description
                inputTypes shouldBe createTaskRequest.parameters().inputTypes
                outputType shouldBe createTaskRequest.parameters().outputType
                methodName shouldBe createTaskRequest.methodName
                level shouldBe Level.fromString(createTaskRequest.level.name)
                startCode shouldBe createTaskRequest.startCodes.mapKeys { (k, v) -> Language.fromString(k) }
            }
            savedTests.captured.zip(createTaskRequest.tests()).forEach { (actual, expected) ->
                actual.inputValues shouldBe expected.inputValues
                actual.outputValue shouldBe expected.outputValue
            }
        }
    }

}
