package r3nny.codest.task.logic

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.dao.TaskDTO
import java.util.*


class CreateTaskOperationTest: OperationTestBase() {
    private val operation = CreateTaskOperation(taskAdapter, driverGenerator)

    @Test
    fun success() = runBlocking {
        with(request) {
            val saved = slot<TaskDTO>()
            coEvery { taskAdapter.createTask(capture(saved)) } returns UUID.randomUUID()
            coEvery { driverGenerator.generate(request) } returns mapOf(
                Language.JAVA to "driver java",
                Language.PYTHON to "driver python"
            )

            operation.activate(this)

            with(saved.captured) {
                name shouldBe request.name
                drivers shouldBe mapOf(
                    Language.JAVA to "driver java",
                    Language.PYTHON to "driver python"
                )
                enabled shouldBe false
                description shouldBe request.description
                parameters shouldBe request.parameters
                startCode shouldBe request.startCode
                tests shouldBe request.tests
            }
        }
    }

}
