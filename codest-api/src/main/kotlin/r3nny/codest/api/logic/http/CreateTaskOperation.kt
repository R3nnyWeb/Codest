package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.dto.extentions.languages
import r3nny.codest.api.dto.extentions.parameters
import r3nny.codest.api.dto.extentions.tests
import r3nny.codest.api.integration.db.TaskAndTestAdapter
import r3nny.codest.api.service.driver.DriverGeneratorService
import r3nny.codest.api.service.validation.validateCreateTask
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.model.CreateTaskRequest
import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class CreateTaskOperation(
    private val taskAndTestAdapter: TaskAndTestAdapter,
    private val driverGenerator: DriverGeneratorService,
) {

    @LogMethod
    open suspend fun activate(request: CreateTaskRequest, userId: UUID): UUID = with(request) {
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
            userId = userId
        )

        taskAndTestAdapter.createTask(task, tests())

        task.id
    }

}