package r3nny.codest.runner.service.executors

import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.config.Logic
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.runner.service.ExecutionResult
import r3nny.codest.runner.service.ProcessRunner
import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.common.Component

@Component
class PythonCodeExecutor(
    private val fileService: CodeFileService,
    private val processRunner: ProcessRunner,
    logic: Logic,
) : CodeExecutor {
    private val languageSettings: Map<Language, LanguageSettings> = logic.languageSettings
    override val languages: Set<Language>
        get() = setOf(Language.PYTHON)

    override suspend fun execute(
        code: String,
        language: Language,
        input: List<String>?,
    ): Pair<ExecutionResult, ExecutionResult> {
        val commandToRun = languageSettings.getValue(language).commandToRun
        val saved = fileService.save(code, "code.py")

        return runCatching {
            val runResult = processRunner.execute(
                commands = listOf(
                    commandToRun,
                    saved.absolutePath
                ),
                input = input
            )

            ExecutionResult(emptyList(), emptyList(), 0) to runResult
        }.also {
            fileService.deleteFolder(saved.parentFile)
        }.getOrThrow()

    }

}