package r3nny.codest.runner.service

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.InvocationException
import java.io.InputStream
import java.util.concurrent.TimeUnit


class ProcessRunner {

    companion object {

        @LogMethod
        fun execute(
            command: String,
            maxTimeSeconds: Long,
            input: List<String>? = null,
        ): Pair<List<String>, List<String>> {
            val processBuilder = processBuilder(command.split("\\s"))
            val process = processBuilder.start()

            input?.let {
                writeToConsole(process, input)
            }

            val isFinishedInTime = process.waitFor(maxTimeSeconds, TimeUnit.SECONDS)

            if (!isFinishedInTime) {
                process.destroy()
                throw InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)
            }

            val code = process.exitValue()

            return if (code == 0) {
                val output = readStream(process.inputStream)
                Pair(output, emptyList())
            } else {
                val output = readStream(process.errorStream)
                Pair(emptyList(), output)
            }

        }

        private fun writeToConsole(process: Process, input: List<String>) {
            process.outputStream.bufferedWriter().use { writer ->
                input.forEach { it ->
                    writer.write(it)
                    writer.newLine()
                }
            }
        }

        @JvmStatic
        fun processBuilder(command: List<String>) = ProcessBuilder(command)

        private fun readStream(inputStream: InputStream?): List<String> {
            return inputStream?.bufferedReader()?.readLines() ?: emptyList()
        }

    }

}
