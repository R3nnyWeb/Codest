package r3nny.codest.runner.operation

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.shared.dto.runner.CoreRunnerErrorType
import r3nny.codest.shared.dto.runner.ExecutableLanguage
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import java.util.UUID

class RunCodeOperation(
    private val codeFileService: CodeFileService,
    private val languageSettings: Map<ExecutableLanguage, LanguageSettings>,
    private val kafkaAdapter: KafkaClientAdapter
) {

    @LogMethod
    suspend fun activate(event: RunCodeRequestEvent, id: UUID) {
        val fileName = "$id.${languageSettings.getValue(event.language).extension}"
        runCatching {
            codeFileService.save(event.code, fileName)
        }.onFailure {
            kafkaAdapter.sendCodeRunResponse(id, RunCodeResponseEvent(
                errorType = CoreRunnerErrorType.INTERNAL_ERROR,
                output = listOf(it.message ?: "internal error")
            ))
        }.getOrThrow()
    }

}