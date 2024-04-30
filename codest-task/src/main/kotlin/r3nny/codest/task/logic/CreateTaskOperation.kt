package r3nny.codest.task.logic

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.common.Level
import java.util.UUID
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.extentions.languages
import r3nny.codest.task.dto.extentions.parameters
import r3nny.codest.task.dto.extentions.tests
import r3nny.codest.task.integration.db.TaskAndTestAdapter
import r3nny.codest.task.model.CreateTaskRequest
import r3nny.codest.task.service.driver.DriverGeneratorService
import r3nny.codest.task.service.validation.validateCreateTask
import ru.tinkoff.kora.common.Component

@Component
open class CreateTaskOperation(
    private val taskAndTestAdapter: TaskAndTestAdapter,
    private val driverGenerator: DriverGeneratorService,
) {

    @LogMethod
    open suspend fun activate(request: CreateTaskRequest): UUID = with(request) {
        validateCreateTask(this)

        val parameters = parameters()
        val mappedLanguages = languages()

        val drivers = driverGenerator.generate(
            methodName = request.methodName,
            parameters = parameters,
            languages = mappedLanguages
        )

        val task = TaskDto(
            id = UUID.randomUUID(),
            name = name,
            drivers = drivers,
            description = description,
            methodName = methodName,
            inputTypes = parameters.inputTypes,
            outputType = parameters.outputType,
            startCode = startCodes.mapKeys { (k, _) -> Language.fromString(k) },
            level = Level.fromString(level.name.lowercase()),
            languages = mappedLanguages,
            isEnabled = false,
            isPrivate = isPrivate ?: false,
        )

        taskAndTestAdapter.createTask(task, tests())

        task.id
    }

}