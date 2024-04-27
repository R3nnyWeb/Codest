package r3nny.codest.runner.operation

import r3nny.codest.runner.config.Logic
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.CodeFileService
import r3nny.codest.runner.service.ProcessRunner
import r3nny.codest.shared.dto.runner.CodeRunnerErrorType
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.util.*

@Component
open class RunCodeOperation(
    private val codeFileService: CodeFileService,
    private val kafkaAdapter: KafkaClientAdapter,
    private val processRunner: ProcessRunner,
    logic: Logic,
) {
    private val languageSettings = logic.languageSettings
    private val maxTime = logic.maxTime

    @Log
    open suspend fun activate(event: RunCodeRequestEvent, id: UUID) {
        val codeExtention = languageSettings.getValue(event.language).codeExtension
        val executeExtention = languageSettings.getValue(event.language).executeExtension
        var pathToFile: String? = null
        var pathToExecute: String? = null
        runCatching {
            pathToFile = codeFileService.save(event.code, codeExtention)
            pathToExecute = pathToFile
            val settings = languageSettings[event.language] ?: throw Exception("Language not found")

            settings.commandToCompile?.let {
                //todo: code helper
                val command = makeCommand(it, settings.argsToCompile, pathToFile)
                val (_, errorOutput, code) = processRunner.execute(command, maxTime)
                if (code != 0)
                    sendError(id, CodeRunnerErrorType.COMPILE_ERROR, output = errorOutput)

                val tmp = if( executeExtention == null ) "" else ".$executeExtention"
                pathToExecute = pathToFile!!.replace(".$codeExtention", tmp)
            }

            val runCommand = makeCommand(settings.commandToRun, settings.argsToRun, pathToExecute)
            val (output, errorOutput, _) = processRunner.execute(runCommand, maxTime, event.input)
            if (errorOutput.isNotEmpty())
                sendError(id, CodeRunnerErrorType.RUNTIME_ERROR, output = output + errorOutput)
            else
                kafkaAdapter.sendCodeRunResponse(id, RunCodeResponseEvent(errorType = null, output = output))

        }.onFailure {
            if (it is InvocationException && it.exceptionCode == InvocationExceptionCode.TIMEOUT_EXCEPTION)
                sendError(id, CodeRunnerErrorType.TIME_EXCEED_ERROR, output = listOf("Время ожидания превышено"))
            else
                sendError(id, CodeRunnerErrorType.INTERNAL_ERROR, it)
        }.also {
            pathToFile?.let {
                codeFileService.delete(it)
            }
            if (pathToExecute != pathToFile)
                pathToExecute?.let {
                    codeFileService.delete(it)
                }
        }
    }

    private fun makeCommand(
        program: String,
        args: String?,
        pathToFile: String?,
    ): String {
        val argsString = if (args == null) "" else "$args "

        return "$program $argsString$pathToFile"
    }

    private suspend fun sendError(
        id: UUID,
        errorType: CodeRunnerErrorType,
        throwable: Throwable? = null,
        output: List<String> = emptyList(),
    ) {
        kafkaAdapter.sendCodeRunResponse(
            id, RunCodeResponseEvent(
                errorType = errorType,
                output = if (throwable == null) output else listOf(throwable.message ?: "internal error"),
            )
        )
    }

}
