package r3nny.codest.task.logic

import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.integration.postgres.AttemptAdapter
import java.util.UUID

class UpdateTaskEnabledOperationTest{
    private val adapter = mockk<AttemptAdapter>(relaxUnitFun = true)
    private val taskAdapter = mockk<TaskAdapter>(relaxUnitFun = true)
    private val taskId = UUID.randomUUID()
    private val sut = UpdateTaskEnabledOperation(adapter, taskAdapter)


    @Test
    fun `success flow - all languages have solution`() = runBlocking {
        coEvery { adapter.getSolvedLanguagesForTask(taskId) } returns Language.values().toList()

        sut.activate(taskId) shouldBe true
        coVerify { taskAdapter.updateTaskEnabled(taskId, true) }
    }

    @Test
    fun `error flow - empty list`() = runBlocking {
        coEvery { adapter.getSolvedLanguagesForTask(taskId) } returns emptyList()

        sut.activate(taskId) shouldBe false
        coVerify { taskAdapter.updateTaskEnabled(taskId, false) }
    }
    @Test
    fun `error flow - not all languages have solution`() = runBlocking {
        coEvery { adapter.getSolvedLanguagesForTask(taskId) } returns listOf(Language.JAVA)

        sut.activate(taskId) shouldBe false
        coVerify { taskAdapter.updateTaskEnabled(taskId, false) }
    }
}