package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.shared.exception.ValidationException
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter
import java.util.*


class CreateTaskOperationTest {
    private val adapter = mockk<TaskAdapter>()
    private val operation = CreateTaskOperation(adapter)

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
                inputValues = listOf("2","2"),
                outputValue = "[2,2]"
            ),
            TestCase(
                inputValues = listOf("2","2"),
                outputValue = "[2,2]"
            ),
        )
    )

    @Test
    fun success(){
        runBlocking {
            with(request){
                coEvery { adapter.createTask(any()) } returns UUID.randomUUID()

                val id = operation.activate(this)
            }
        }
    }



}