package r3nny.codest.api.logic

import io.mockk.mockk
import r3nny.codest.api.cache.GetAttemptCache
import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.dto.extentions.languages
import r3nny.codest.api.dto.extentions.parameters
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.api.integration.db.TaskAndTestAdapter
import r3nny.codest.api.integration.db.TestAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.api.service.driver.DriverGeneratorService
import r3nny.codest.model.CreateTaskRequest
import r3nny.codest.model.CreateTest
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import java.util.*

abstract class OperationTestBase {
    internal val taskAndTestAdapter = mockk<TaskAndTestAdapter>(relaxUnitFun = true)
    internal val taskAdapter = mockk<TaskAdapter>(relaxUnitFun = true)
    internal val attemptsAdapter: AttemptsAdapter = mockk<AttemptsAdapter>(relaxUnitFun = true)
    internal val kafkaAdapter: KafkaAdapter = mockk(relaxUnitFun = true)
    internal val getAttemptCache: GetAttemptCache = mockk<GetAttemptCache>(relaxUnitFun = true)
    internal val testAdapter = mockk<TestAdapter>(relaxUnitFun = true)
    internal val driverGenerator = mockk<DriverGeneratorService>(relaxUnitFun = true)

    internal val createTaskRequest = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        inputTypes = listOf(Type.INTEGER, Type.INTEGER).map(Type::name).toList(),
        outputType = Type.INTEGER_ARR.name,
        languages = listOf(Language.JAVA.name, Language.PYTHON.name),
        startCodes = mapOf(
            Language.JAVA.name to "some start java code",
            Language.PYTHON.name to "some start python code"
        ),
        level = r3nny.codest.model.Level.EASY,
        tests = listOf(
            CreateTest(
                inputData = listOf("2", "2"),
                outputData = "[2,2]"
            ),
            CreateTest(
                inputData = listOf("2", "2"),
                outputData = "[2,2]"
            ),
        )
    )

    internal val saved = TaskDto(
        id = UUID.randomUUID(),
        name = createTaskRequest.name,
        drivers = mapOf(
            Language.JAVA to "driver \${solution} java",
            Language.PYTHON to "driver \${solution} python"
        ),
        description = createTaskRequest.description,
        inputTypes = createTaskRequest.parameters().inputTypes,
        outputType = createTaskRequest.parameters().outputType,
        startCode = createTaskRequest.startCodes.mapKeys { (k, v) -> Language.fromString(k) },
        methodName = createTaskRequest.methodName,
        level = Level.EASY,
        languages = createTaskRequest.languages(),
        isEnabled = false,
        isPrivate = createTaskRequest.isPrivate ?: false
    )

    internal val stubTests = listOf(
        TestCase(
            id = UUID.randomUUID(),
            inputValues = listOf("2", "2"),
            outputValue = "2"
        ),
        TestCase(
            id = UUID.randomUUID(),
            inputValues = listOf("2", "2"),
            outputValue = "2"
        )
    )

}
