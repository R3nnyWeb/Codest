package r3nny.codest.task.logic

import io.mockk.mockk
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.integration.postgres.AttemptAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService
import java.util.*

abstract class OperationTestBase {
    internal val taskAdapter = mockk<TaskAdapter>(relaxUnitFun = true)
        internal val attemptAdapter = mockk<AttemptAdapter>(relaxUnitFun = true)
    internal val driverGenerator = mockk<DriverGeneratorService>(relaxUnitFun = true)

    internal val request = CreateTaskRequest(
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

}
