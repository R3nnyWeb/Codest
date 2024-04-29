package r3nny.codest.task.service.driver

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.task.builder.buildGenerator
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequestDto
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class DriverGeneratorService(
    private val config: AppConfig,
) {

    @Log
    open suspend fun generate(request: CreateTaskRequestDto): Map<Language, String>
        = generate(request.methodName, request.parameters, request.languages)

    @Log
    open suspend fun generate(methodName: String, parameters: TaskParameters, languages: Set<Language>): Map<Language, String> {
         val result = mutableMapOf<Language, String>()

        val deferreds = coroutineScope {
            languages.map { language ->
                async {
                    language to buildGenerator(language, config).generate(methodName, parameters)
                }
            }
        }
        result += deferreds.awaitAll()
        return result
    }

}
