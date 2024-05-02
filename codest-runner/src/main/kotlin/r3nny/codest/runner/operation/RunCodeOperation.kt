package r3nny.codest.runner.operation

import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.integration.KafkaClientAdapter
import r3nny.codest.runner.service.ExecutionResultTester
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
    private val tester: ExecutionResultTester,
    executors: All<CodeExecutor>,
) {
    private val executorsMap = executors.flatMap { executor ->
        executor.languages.map { language -> language to executor }
    }.toMap()

    @Log
    open suspend fun activate(event: RunCodeRequestEvent, id: UUID) {
        runCatching {
            val executor = executorsMap[event.language]!!

            val (compileResult, runResult) = executor.execute(event)
            with(compileResult) {
                if (exitCode != 0) {
                    sendError(id, CodeRunnerErrorType.COMPILE_ERROR, output = errorOutput)
                    return@runCatching
                }

            }
            with(runResult) {
                if (errorOutput.isEmpty()) {
                    val testErrors = tester.findError(event.tests, output)
                    if (testErrors == null) {
                        kafkaAdapter.sendCodeRunResponse(id, RunCodeResponseEvent(output = output))
                    } else {
                        sendError(id, CodeRunnerErrorType.TEST_ERROR, output = listOf(testErrors.first, testErrors.second))
                    }
                } else {
                    sendError(id, CodeRunnerErrorType.RUNTIME_ERROR, output = output + errorOutput)
                }

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
