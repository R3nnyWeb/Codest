package r3nny.codest.task.integration

import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import java.util.UUID
import org.testcontainers.junit.jupiter.Testcontainers
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.dao.Level
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequestDto

inline fun <reified T> ValidatableResponse.extractAs(): T {
    return this.extract().body().`as`(T::class.java)
}

inline fun <reified T> Response.extractAs(): T {
    return this.then().extract().body().`as`(T::class.java)
}

@Testcontainers
abstract class TestBase {
//    @LocalServerPort
         internal var port: Int = 0
//
//    @Autowired
//    internal lateinit var taskRepository: TaskRepository
//    @Autowired
//    internal lateinit var createTaskOperation: CreateTaskOperation

    internal fun url() = "http://localhost:$port/api/v1/tasks"

    internal val request = CreateTaskRequestDto(
        name = "task",
        description = "some md descr",
        methodName = "method",
        parameters = TaskParameters(
            inputTypes = listOf(Type.INTEGER, Type.INTEGER),
            outputType = Type.STRING
        ),
        startCode = mapOf(
            Language.JAVA to "some start java code",
            Language.PYTHON to "some start python code"
        ),
        level = Level.EASY,
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

    internal val saved = TaskDTO(
        id = UUID.randomUUID(),
        name = request.name,
        drivers = mapOf(
            Language.JAVA to "driver java",
            Language.PYTHON to "driver python"
        ),
        description = request.description,
        parameters = request.parameters,
        startCode = request.startCode,
        methodName = request.methodName,
        level = request.level,
        tests = request.tests
    )
}
