package r3nny.codest.runner.service

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.InvocationException
import java.util.concurrent.TimeUnit


class ProcessRunner(
    private val maxTimeSeconds: Long,
) {


    @LogMethod
    fun execute(command: String): Pair<String, String> {
        val processBuilder = processBuilder(command.split("\\s"))
        val process = processBuilder.start()

        val isFinishedInTime = process.waitFor(maxTimeSeconds, TimeUnit.SECONDS)

        if (!isFinishedInTime) {
            process.destroy()
            throw InvocationException(InvocationExceptionCode.TIMEOUT_EXCEPTION)
        }

        val code = process.exitValue()

        return if (code == 0) {
            Pair(process.inputStream.bufferedReader().readText(), "")
        } else {
            Pair("", process.errorStream.bufferedReader().readText())
        }

    }

    companion object {

        @JvmStatic
        fun processBuilder(command: List<String>) = ProcessBuilder(command)

    }

}
