package r3nny.codest.task.service.driver

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.task.builder.buildGenerator
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequest

@Service
class DriverGeneratorService(
    private val config: AppConfig,
) {

    @LogMethod
    suspend fun generate(request: CreateTaskRequest): Map<Language, String>
        = generate(request.methodName, request.parameters, request.languages)

    @LogMethod
    suspend fun generate(methodName: String, parameters: TaskParameters, languages: Set<Language>): Map<Language, String> {
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
