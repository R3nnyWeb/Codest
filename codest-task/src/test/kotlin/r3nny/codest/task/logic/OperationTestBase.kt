package r3nny.codest.task.logic

import io.mockk.mockk
import java.util.UUID
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.dao.Level
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequestDto
import r3nny.codest.task.integration.db.TaskAdapter
import r3nny.codest.task.service.driver.DriverGeneratorService

abstract class OperationTestBase {
    internal val taskAdapter = mockk<TaskAdapter>(relaxUnitFun = true)
    internal val driverGenerator = mockk<DriverGeneratorService>(relaxUnitFun = true)

    internal val request = CreateTaskRequestDto(
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
        level = Level.EASY,
        tests = request.tests
    )

}
