package r3nny.codest.runner.operation

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test
import r3nny.codest.runner.config.LanguageSettings
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.shared.dto.runner.CoreRunnerErrorType
import r3nny.codest.shared.dto.runner.ExecutableLanguage
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import java.util.UUID

class RunCodeOperationTest {
    private val id = UUID.randomUUID()
    private val event = RunCodeRequestEvent(
        code = "some code",
        input = listOf("input", "some"),
        language = ExecutableLanguage.JAVA_17
    )
    private val fileService = mockk<CodeFileService>(relaxUnitFun = true)
    private val languageSettings = mapOf(
        ExecutableLanguage.JAVA_17 to LanguageSettings(
            "javac17",
            null,
            "java17",
            null,
            "java"
        )
    )
    private val kafkaAdapter = mockk<KafkaClientAdapter>()
    private val operation = RunCodeOperation(
        codeFileService = fileService,
        languageSettings = languageSettings,
        kafkaAdapter = kafkaAdapter
    )

    @Test
    fun error_while_creating_file(): Unit = runBlocking {
        coEvery { fileService.save(event.code, "${event.code}.${languageSettings[event.language]!!.extension}") }throws
                InvocationException(InvocationExceptionCode.FILE_WRITE_ERROR)

        shouldThrow<InvocationException> {
            operation.activate(event, UUID.randomUUID())
        }

        coVerify {
            kafkaAdapter.sendCodeRunResponse(id, RunCodeResponseEvent(
                errorType = CoreRunnerErrorType.INTERNAL_ERROR,
                output = listOf(InvocationExceptionCode.FILE_WRITE_ERROR.message)
            ))
        }

    }
}