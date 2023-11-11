package r3nny.codest.task.controller.http

import kotlinx.coroutines.runBlocking
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.logic.CreateTaskOperation
import r3nny.codest.task.service.driver.DriverGeneratorService
import java.util.UUID

@RestController
class TaskController(
    private val createTaskOperation: CreateTaskOperation
) {

     private val request = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        parameters = TaskParameters(
            inputTypes = listOf(Type.INTEGER, Type.INTEGER),
            outputType = Type.INTEGER
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

    @LogMethod
    @GetMapping("/")
    //todo: Reactive?
    fun test(): UUID = runBlocking {
        createTaskOperation.activate(request)
    }

}