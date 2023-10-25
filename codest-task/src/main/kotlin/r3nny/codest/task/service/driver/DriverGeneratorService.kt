package r3nny.codest.task.service.driver

import jdk.jshell.spi.ExecutionControl.NotImplementedException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.service.driver.internal.JavaDriverGenerator
import r3nny.codest.task.service.driver.internal.LanguageDriverGenerator

class DriverGeneratorService(
    private val config: AppConfig
) {
    //TODO: Подумать как это потестить. Ну типо нахера опять код проверять, если все LanguageDriverGenerator проверены
    suspend fun generate(request: CreateTaskRequest): Map<Language, String> {
        val languages = Language.values()
        val result = mutableMapOf<Language, String>()

        val deferreds = coroutineScope {
             languages.map { language ->
                 async {
                     language to LanguageDriverGenerator.buildGenerator(language, config).generate(request)
                 }
             }
        }
        result += deferreds.awaitAll()
        return result
    }


}