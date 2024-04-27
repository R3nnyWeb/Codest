package r3nny.codest.runner.service

import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.InvocationException
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.io.InputStream
import java.util.concurrent.TimeUnit

@Component
open class ProcessRunner {

    data class Result(
        val output: List<String>,
        val errorOutput: List<String>,
        val exitCode: Int,
    )


    @Log
    open fun execute(
        command: String,
        maxTimeSeconds: Long,
        input: List<String>? = null,
    ): Result {
        val processBuilder = processBuilder(command.split(" "))
        val process = processBuilder.start()

        input?.let {
            writeToConsole(process, input)
        }

        val isFinishedInTime = process.waitFor(maxTimeSeconds, TimeUnit.SECONDS)

        if (!isFinishedInTime) {
            process.destroy()
            throw InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)
        }

        return Result(
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
