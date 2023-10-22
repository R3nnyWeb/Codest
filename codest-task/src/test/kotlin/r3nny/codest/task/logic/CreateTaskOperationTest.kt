package r3nny.codest.task.logic

import io.kotest.assertions.throwables.shouldThrow
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


class CreateTaskOperationTest{
    val adapter = mockk<TaskAdapter>()
    val operation = CreateTaskOperation(adapter)

    val request =  CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        parameters = TaskParameters(
            inputTypes =  listOf(Type.INTEGER, Type.INTEGER),
            outputTypes = Type.INTEGER_ARR
        ),
        startCode = mapOf(
            Language.JAVA to "some start java code",
            Language.PYTHON to "some start python code"
        ),
        tests = listOf(
            TestCase(
                inputValues = listOf("2,2"),
                outputValue = "[2,2]"
            ),
            TestCase(
                inputValues = listOf("2,2"),
                outputValue = "[2,2]"
            ),
        )
    )

    //error - test input params neq params
    //error - tests less then two
    //error start code not for all
    @Test
    fun `error - input params empty`(): Unit = runBlocking{
        with(request.copy(
            parameters = TaskParameters(
                inputTypes = listOf(),
                outputTypes = Type.INTEGER_ARR
            ))){

            shouldThrow<ValidationException>{
                operation.activate(this)
            }
        }
    }

}