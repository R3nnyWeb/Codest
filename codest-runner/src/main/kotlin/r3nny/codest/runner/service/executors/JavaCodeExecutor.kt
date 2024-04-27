package r3nny.codest.runner.service.executors

import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.runner.service.ExecutionResult
import r3nny.codest.runner.service.ProcessRunner
import r3nny.codest.shared.dto.runner.ExecutableLanguage
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log

@Component
open class JavaCodeExecutor(
    private val fileService: CodeFileService,
    private val processRunner: ProcessRunner,
    private val languageSettings: Map<ExecutableLanguage, LanguageSettings>
) : CodeExecutor {
    override val languages: Set<ExecutableLanguage>
        get() = setOf(ExecutableLanguage.JAVA_17, ExecutableLanguage.JAVA_8)

    @Log
    override suspend fun execute(code: String, language: ExecutableLanguage, input: List<String>?): Pair<ExecutionResult, ExecutionResult> {
        val commandToCompile = languageSettings.getValue(language).commandToCompile!!
        val saved = fileService.save(code, "Main.java")
        val compileResult = processRunner.execute(
            commands = listOf(
                commandToCompile,
                saved.absolutePath
            )
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

        return compileResult to runResult
    }
}