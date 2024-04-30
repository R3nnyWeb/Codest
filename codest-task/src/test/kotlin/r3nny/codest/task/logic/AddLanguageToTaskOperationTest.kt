package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.common.TaskParameters
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.http.AddLanguageToTaskRequest
import java.util.*

class AddLanguageToTaskOperationTest : OperationTestBase() {

    private val operation = AddLanguageToTaskOperation(driverGenerator, taskAdapter)
    private val id = UUID.randomUUID()
    private val addLanguageRequest = AddLanguageToTaskRequest(Language.JAVA, "some java")

    @Test
    fun `success flow - add new language and generate drivers`() {
        runBlocking {
            val savedWithOneLanguage = getTaskWithOnlyLanguage(saved, Language.PYTHON)
            val slot = slot<TaskDto>()
            coEvery { taskAdapter.getFullById(id) } returns savedWithOneLanguage
            coEvery {
                driverGenerator.generate(
                    methodName = createTaskRequest.methodName,
                    parameters = TaskParameters(
                        inputTypes = saved.inputTypes,
                        outputType = saved.outputType
                    ),
                    languages = setOf(addLanguageRequest.language)
                )
            } returns mapOf(addLanguageRequest.language to "driver")

            operation.activate(id, addLanguageRequest)


            coVerify { taskAndTestAdapter.updateLanguage(capture(slot)) }
            val updated = slot.captured
            with(updated){
                isEnabled shouldBe false
                drivers shouldBe savedWithOneLanguage.drivers + mapOf(addLanguageRequest.language to "driver")
            }


        }
    }


    @Test
    fun `error flow - language exists`() {
        runBlocking {
            coEvery { taskAdapter.getFullById(id) } returns saved

            //business exception - 422
            shouldThrowAny {
                operation.activate(id, addLanguageRequest)
            }
        }
    }

    private fun getTaskWithOnlyLanguage(task: TaskDto, language: Language) =
        task.copy(
            drivers = mapOf(language to "driver"),
            startCode = mapOf(language to "start code"),
            isEnabled = true
        )

}