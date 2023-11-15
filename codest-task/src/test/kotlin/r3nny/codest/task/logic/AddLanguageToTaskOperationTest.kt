package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.AddLanguageToTaskRequest
import java.util.UUID

class AddLanguageToTaskOperationTest : OperationTestBase() {

    private val operation = AddLanguageToTaskOperation(driverGenerator)
    private val id = UUID.randomUUID()
    private val addLanguageRequest = AddLanguageToTaskRequest(Language.JAVA, "some java")

    @Test
    fun `success flow - add new language and generate drivers`() {
        runBlocking {
            val savedWithOneLanguage = getTaskWithOnlyLanguage(saved, Language.PYTHON)
            val slot = slot<TaskDTO>()
            coEvery { taskAdapter.getById(id) } returns savedWithOneLanguage
            coEvery {
                driverGenerator.generate(
                    methodName = request.methodName,
                    parameters = saved.parameters,
                    languages = setOf(addLanguageRequest.language)
                )
            } returns mapOf(addLanguageRequest.language to "driver")

            operation.activate(id, addLanguageRequest)


            coVerify { taskAdapter.update(capture(slot)) }
            val updated = slot.captured
            with(updated){
                drivers.keys shouldBe
            }


        }
    }


    @Test
    fun `error flow - language exists`() {
        runBlocking {
            coEvery { taskAdapter.getById(id) } returns saved

            //business exception - 422
            shouldThrowAny {
                operation.activate(id, addLanguageRequest)
            }
        }
    }

    private fun getTaskWithOnlyLanguage(task: TaskDTO, language: Language) =
        task.copy(
            drivers = mapOf(language to "driver"),
            startCode = mapOf(language to "start code")
        )

}