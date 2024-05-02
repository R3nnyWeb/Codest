package r3nny.codest.runner.service.executors

import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.config.Logic
import r3nny.codest.runner.helper.inputs
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.runner.service.ExecutionResult
import r3nny.codest.runner.service.ProcessRunner
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class JavaCodeExecutor(
    private val fileService: CodeFileService,
    private val processRunner: ProcessRunner,
    logic: Logic,
) : CodeExecutor {
    private val languageSettings: Map<Language, LanguageSettings> = logic.languageSettings
    override val languages: Set<Language>
        get() = setOf(Language.JAVA)

    @Log
    override suspend fun execute(
        code: String,
        language: Language,
        input: List<String>?,
    ): Pair<ExecutionResult, ExecutionResult> {
        val commandToCompile = languageSettings.getValue(language).commandToCompile!!
        val saved = fileService.save(code, "Main.java")
        return runCatching {
            val compileResult = processRunner.execute(
                commands = listOf(
                    commandToCompile,
                    saved.absolutePath
                )
            )
            if (compileResult.exitCode != 0) return@runCatching compileResult to ExecutionResult(
                emptyList(),
                emptyList(),
                1
            )

            val commandToRun = languageSettings.getValue(language).commandToRun!!
            val runResult = processRunner.execute(
                commands = listOf(
                    commandToRun,
                    "-cp",
                    saved.parent,
                    "Main"
                ),
                input = input
            )
            compileResult to runResult
        }.also {
            fileService.deleteFolder(saved.parentFile)
        }.getOrThrow()
    }

    override suspend fun execute(request: RunCodeRequestEvent): Pair<ExecutionResult, ExecutionResult> {
        return execute(request.code, request.language, request.inputs())
    }
}