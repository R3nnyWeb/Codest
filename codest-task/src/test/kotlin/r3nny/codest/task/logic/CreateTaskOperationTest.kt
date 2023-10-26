package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.shared.exception.ValidationException
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import java.util.*


class CreateTaskOperationTest {
    private val adapter = mockk<TaskAdapter>()
    private val driverGenerator = mockk<DriverGeneratorService>()
    private val operation = CreateTaskOperation(adapter, driverGenerator)

    private val request = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        parameters = TaskParameters(
            inputTypes = listOf(Type.INTEGER, Type.INTEGER),
            outputType = Type.INTEGER_ARR
        ),
        startCode = mapOf(
            Language.JAVA to "some start java code",
            Language.PYTHON to "some start python code"
        ),
        tests = listOf(
            TestCase(
                inputValues = listOf("2", "2"),
                outputValue = "[2,2]"
            ),
            TestCase(
                inputValues = listOf("2", "2"),
                outputValue = "[2,2]"
            ),
        )
    )

    @Test
    fun success() = runBlocking {
        with(request) {
            val saved = slot<TaskDTO>()
            coEvery { adapter.createTask(capture(saved)) } returns UUID.randomUUID()
            coEvery { driverGenerator.generate(request) } returns mapOf(
                Language.JAVA to "driver java",
                Language.PYTHON to "driver python"
            )

            val id = operation.activate(this)

            with(saved.captured) {
                name shouldBe request.name
                drivers shouldBe mapOf(
                    Language.JAVA to "driver java",
                    Language.PYTHON to "driver python"
                )
                description shouldBe request.description
                parameters shouldBe request.parameters
                tests shouldBe request.tests
            }
        }
    }


}