package r3nny.codest.runner.operation

import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.executors.CodeExecutor
import r3nny.codest.shared.dto.runner.CodeRunnerErrorType
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import r3nny.codest.shared.exception.InvocationException
import ru.tinkoff.kora.application.graph.All
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.util.*

@Component
open class RunCodeOperation(
    private val kafkaAdapter: KafkaClientAdapter,
    executors: All<CodeExecutor>,
) {
    private val executorsMap = executors.flatMap { executor ->
        executor.languages.map { language -> language to executor }
    }.toMap()

    @Log
    open suspend fun activate(event: RunCodeRequestEvent, id: UUID) {
        runCatching {
            val executor = executorsMap[event.language]!!

            val (compileResult, runResult) = executor.execute(event.code, event.language, event.input)
            with(compileResult) {
                if (exitCode != 0)
                    sendError(id, CodeRunnerErrorType.COMPILE_ERROR, output = errorOutput)
            }
            with(runResult) {
                if (errorOutput.isNotEmpty())
                    sendError(id, CodeRunnerErrorType.RUNTIME_ERROR, output = output + errorOutput)
                else
                    kafkaAdapter.sendCodeRunResponse(id, RunCodeResponseEvent(errorType = null, output = output))
            }

        }.onFailure {
            if (it is InvocationException && it.exceptionCode == InvocationExceptionCode.TIMEOUT_EXCEPTION)
                sendError(id, CodeRunnerErrorType.TIME_EXCEED_ERROR, output = listOf("Время ожидания превышено"))
            else
                sendError(id, CodeRunnerErrorType.INTERNAL_ERROR, it)
        }
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
