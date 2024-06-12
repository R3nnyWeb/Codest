package r3nny.codest.runner.service

import org.slf4j.LoggerFactory
import r3nny.codest.runner.config.LogicConfigMapping
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.InvocationException
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.io.InputStream
import java.util.concurrent.TimeUnit

data class ExecutionResult(
    val output: List<String>,
    val errorOutput: List<String>,
    val exitCode: Int,
)

@Component
open class ProcessRunner(
    private val logic: LogicConfigMapping,
) {

    private val logger = LoggerFactory.getLogger(ProcessRunner::class.java)

    @Log
    open fun execute(
        commands: List<String>,
        maxTimeSeconds: Long = logic.maxTime(),
        input: List<String>? = null,
    ): ExecutionResult {
        val processBuilder = processBuilder(commands)
        logger.info("Executing ${commands.joinToString(" ")}")
        val process = processBuilder.start()

        input?.let {
            writeToConsole(process, input)
        }

        val isFinishedInTime = process.waitFor(maxTimeSeconds, TimeUnit.SECONDS)

        if (!isFinishedInTime) {
                        logger.info("Process timed out")
            process.destroy()

            throw InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)
        }

        return ExecutionResult(
            output = readStream(process.inputStream),
            errorOutput = readStream(process.errorStream),
            exitCode = process.exitValue(),
        )
    }

    private fun writeToConsole(process: Process, input: List<String>) {
        process.outputStream.bufferedWriter().use { writer ->
            input.forEach { it ->
                writer.write(it)
                writer.newLine()
            }
        }
    }


    private fun readStream(inputStream: InputStream?): List<String> {
        return inputStream?.bufferedReader()?.readLines() ?: emptyList()
    }

    companion object {
        @JvmStatic
        fun processBuilder(command: List<String>) = ProcessBuilder(command)
    }

}
