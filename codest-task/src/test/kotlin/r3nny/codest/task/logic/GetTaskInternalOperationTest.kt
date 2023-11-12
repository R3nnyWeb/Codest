package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import java.util.*

class GetTaskInternalOperationTest : OperationTestBase() {
    private val operation = GetTaskInternalOperation(taskAdapter)
    private val taskId = UUID.randomUUID()

    @Test
    fun `success flow - return driver`() = runBlocking {
        coEvery { taskAdapter.getById(taskId) } returns saved

        val response = operation.activate(taskId, Language.JAVA)

        with(response) {
            taskId shouldBe saved.id
            driver shouldBe saved.drivers[Language.JAVA]
            tests shouldBe saved.tests
        }

    }

    @Test
    fun `error flow - not found`(): Unit = runBlocking {
        coEvery { taskAdapter.getById(taskId) } returns null

        //todo: Exceptions
        shouldThrowAny {
            operation.activate(taskId, Language.JAVA)
        }
    }

        @Test
    fun `error flow - driver not exists`(): Unit = runBlocking {
        with(saved.copy(drivers = mapOf(Language.PYTHON to "driver"))) {
            coEvery { taskAdapter.getById(taskId) } returns this

            shouldThrowAny {
                operation.activate(taskId, Language.JAVA)
            }
        }
    }

}
