package r3nny.codest.runner.operation

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.config.Logic
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.runner.service.ProcessRunner
import r3nny.codest.shared.dto.runner.CodeRunnerErrorType
import r3nny.codest.shared.dto.runner.ExecutableLanguage
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import java.util.*

class RunCodeOperationTest {
    private val id = UUID.randomUUID()
    private val event = RunCodeRequestEvent(
        code = "some code", input = listOf("input", "some"), language = ExecutableLanguage.JAVA_17
    )
    private val languageSettings = mapOf(
        ExecutableLanguage.JAVA_17 to LanguageSettings(
            "javac17", null, "java17", null, "java", "class"
        )

    )
    private val codeExtension = languageSettings[ExecutableLanguage.JAVA_17]!!.codeExtension
    private val executeExtension = languageSettings[ExecutableLanguage.JAVA_17]!!.executeExtension
    private val pathToCode = "somePath.$codeExtension"
    private val pathToExecute = "somePath.$executeExtension"
    private val fileService = mockk<CodeFileService>(relaxUnitFun = true) {
        coEvery { this@mockk.save(event.code, codeExtension) } returns pathToCode
    }
    private val processRunner = mockk<ProcessRunner>(relaxUnitFun = true)
    private val logic = Logic(
        languageSettings = languageSettings, maxTime = 1L
    )
    private val kafkaAdapter = mockk<KafkaClientAdapter>(relaxUnitFun = true)
    private val operation = RunCodeOperation(
        codeFileService = fileService,
        logic = logic,
        kafkaAdapter = kafkaAdapter,
        processRunner = processRunner
    )

    @Test
    fun success_flow(): Unit = runBlocking {
        coEvery { fileService.save(event.code, codeExtension) } returns pathToCode
        coEvery { compile() } returns successCompile()
        coEvery { run() } returns ProcessRunner.Result(
            listOf("some"), emptyList(), 0
        )

        operation.activate(event, id)

        coVerify {
            compile()
            kafkaAdapter.sendCodeRunResponse(
                id, RunCodeResponseEvent(
                    errorType = null, output = listOf("some")
                )
            )
            shouldDeleteFiles()
        }
    }

    @Test
    fun run_timeout_error(): Unit = runBlocking {
        coEvery { fileService.save(event.code, codeExtension) } returns pathToCode
        coEvery { compile() } returns successCompile()
        coEvery { run() } throws InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)

        operation.activate(event, id)

        coVerify {
            compile()
            kafkaAdapter.sendCodeRunResponse(
                id, RunCodeResponseEvent(
                    errorType = CodeRunnerErrorType.TIME_EXCEED_ERROR, output = listOf("Время ожидания превышено")
                )
            )
            shouldDeleteFiles()
        }
    }

    private suspend fun shouldDeleteFiles() {
        fileService.delete(pathToCode)
        fileService.delete(pathToExecute)
    }

    @Test
    fun runtime_error(): Unit = runBlocking {
        coEvery { compile() } returns successCompile()
        coEvery { run() } returns ProcessRunner.Result(
            listOf("some"), listOf("error"), 1
        )

        operation.activate(event, id)

        coVerify {
            run()
            kafkaAdapter.sendCodeRunResponse(
                id, RunCodeResponseEvent(
                    errorType = CodeRunnerErrorType.RUNTIME_ERROR, output = listOf("some", "error")
                )
            )
            shouldDeleteFiles()
        }
    }

    private fun run() = processRunner.execute(
        "java17 $pathToExecute", logic.maxTime, event.input
    )

    private fun successCompile() = ProcessRunner.Result(
        emptyList(), emptyList(), 0
    )

    @Test
    fun compile_timeout_error(): Unit = runBlocking {
        coEvery { compile() } throws InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)

        operation.activate(event, id)

        coVerify {
            kafkaAdapter.sendCodeRunResponse(
                id, RunCodeResponseEvent(
                    errorType = CodeRunnerErrorType.TIME_EXCEED_ERROR, output = listOf("Время ожидания превышено")
                )
            )
            fileService.delete(pathToCode)
        }
    }

    @Test
    fun compile_error(): Unit = runBlocking {
        coEvery { compile() } returns ProcessRunner.Result(
            emptyList(), listOf("error"), 1
        )

        operation.activate(event, id)

        coVerify {
            kafkaAdapter.sendCodeRunResponse(
                id, RunCodeResponseEvent(
                    errorType = CodeRunnerErrorType.COMPILE_ERROR, output = listOf("error")
                )
            )
            shouldDeleteFiles()
        }
    }

    private fun compile() = processRunner.execute(
        "javac17 $pathToCode",
        logic.maxTime,
    )

    @Test
    fun error_while_creating_file(): Unit = runBlocking {
        coEvery {
            fileService.save(
                event.code, codeExtension
            )
        } throws InvocationException(InvocationExceptionCode.FILE_WRITE_ERROR)

        operation.activate(event, id)

        coVerify {
            kafkaAdapter.sendCodeRunResponse(
                id, RunCodeResponseEvent(
                    errorType = CodeRunnerErrorType.INTERNAL_ERROR,
                    output = listOf(InvocationExceptionCode.FILE_WRITE_ERROR.message)
                )
            )
        }

    }

}
