package r3nny.codest.runner.service.executors

import org.slf4j.LoggerFactory
import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.config.Logic
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.runner.service.ExecutionResult
import r3nny.codest.runner.service.ProcessRunner
import r3nny.codest.shared.domain.Language
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

    private val logger = LoggerFactory.getLogger(JavaCodeExecutor::class.java)

    @Log
    override suspend fun execute(
        code: String,
        language: Language,
        input: List<String>?,
    ): Pair<ExecutionResult, ExecutionResult> {
        val commandToCompile = languageSettings.getValue(language).commandToCompile!!
        val saved = fileService.save(code, "Driver.java")
        logger.info("Saved to ${saved.absolutePath}")
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

            val commandToRun = languageSettings.getValue(language).commandToRun
            val runResult = processRunner.execute(
                commands = listOf(
                    commandToRun,
                    "-cp",
                    saved.parent,
                    "Driver"
                ),
                input = input
            )
            compileResult to runResult
        }.also {
            fileService.deleteFolder(saved.parentFile)
        }.getOrThrow()
    }
}