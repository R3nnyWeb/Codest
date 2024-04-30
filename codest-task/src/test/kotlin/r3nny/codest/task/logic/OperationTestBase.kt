package r3nny.codest.task.logic

import io.mockk.mockk
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.common.Level
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.extentions.languages
import r3nny.codest.task.dto.extentions.parameters
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.integration.db.TaskAndTestAdapter
import r3nny.codest.task.integration.db.TestAdapter
import r3nny.codest.task.model.CreateTaskRequest
import r3nny.codest.task.model.CreateTest
import r3nny.codest.task.service.driver.DriverGeneratorService
import java.util.*

abstract class OperationTestBase {
    internal val taskAndTestAdapter = mockk<TaskAndTestAdapter>(relaxUnitFun = true)
    internal val taskAdapter = mockk<TaskAdapter>(relaxUnitFun = true)
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
        level = r3nny.codest.task.model.Level.EASY,
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
            Language.JAVA to "driver java",
            Language.PYTHON to "driver python"
        ),
        description = createTaskRequest.description,
        inputTypes = createTaskRequest.parameters().inputTypes,
        outputType = createTaskRequest.parameters().outputType,
        startCode = createTaskRequest.startCodes.mapKeys { (k,v) -> Language.fromString(k) },
        methodName = createTaskRequest.methodName,
        level = Level.EASY,
        languages = createTaskRequest.languages(),
        isEnabled = false,
        isPrivate = createTaskRequest.isPrivate ?: false
    )

}
