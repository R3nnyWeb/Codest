package r3nny.codest.task.integration

import io.restassured.response.Response
import io.restassured.response.ValidatableResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskRepository
import r3nny.codest.task.logic.CreateTaskOperation
import java.util.*

inline fun <reified T> ValidatableResponse.extractAs(): T {
    return this.extract().body().`as`(T::class.java)
}

inline fun <reified T> Response.extractAs(): T {
    return this.then().extract().body().`as`(T::class.java)
}

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class TestBase {
    @LocalServerPort
    internal var port: Int = 0

    @Autowired
    internal lateinit var taskRepository: TaskRepository
    @Autowired
    internal lateinit var createTaskOperation: CreateTaskOperation

    internal fun url() = "http://localhost:$port/api/v1/tasks"

    internal val request = CreateTaskRequest(
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
        tests = request.tests
    )

    companion object {
        @Container
        @ServiceConnection
        var mongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:7.0"))
    }


}