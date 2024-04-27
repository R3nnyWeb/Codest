package r3nny.codest.runner.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.InvocationException
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

class ProcessRunnerTest {
    private val processRunner: ProcessRunner = ProcessRunner()


    @Test
    fun `success flow`() {
        every { process.inputStream } returns ByteArrayInputStream("success".toByteArray())
        every { process.errorStream } returns ByteArrayInputStream(ByteArray(0))
        every { process.waitFor(3, TimeUnit.SECONDS) } returns true
        every { process.exitValue() } returns 0

        val (output, errorOutput, code) = processRunner.execute(command, 3)

        errorOutput.isEmpty() shouldBe true
        code shouldBe 0
        output shouldBe listOf("success")
    }

    @Test
    fun `success flow - with input`() {
        every { process.inputStream } returns ByteArrayInputStream("success".toByteArray())
        every { process.waitFor(3, TimeUnit.SECONDS) } returns true
        every { process.exitValue() } returns 0
        val outputStream = ByteArrayOutputStream()
        every { process.outputStream } returns outputStream

        val (output, errorOutput, code) = processRunner.execute(command, 3, listOf("input", "some"))

        outputStream.toString() shouldBe "input\r\nsome\r\n"
        errorOutput.isEmpty() shouldBe true
        code shouldBe 0
        output shouldBe listOf("success")
    }

    @Test
    fun `error flow`() {
        every { process.errorStream } returns ByteArrayInputStream("error".toByteArray())
        every { process.waitFor(3, TimeUnit.SECONDS) } returns true
        every { process.exitValue() } returns 1

        val (output, errorOutput, code) = processRunner.execute(command, 3)

        output.isEmpty() shouldBe true
        code shouldBe 1
        errorOutput shouldBe listOf("error")
    }

    @Test
    fun `error flow - with output`() {
        every { process.errorStream } returns ByteArrayInputStream("error".toByteArray())
        every { process.inputStream } returns ByteArrayInputStream("some".toByteArray())
        every { process.waitFor(3, TimeUnit.SECONDS) } returns true
        every { process.exitValue() } returns 1

        val (output, errorOutput, code) = processRunner.execute(command, 3)

        output shouldBe listOf("some")
        code shouldBe 1
        errorOutput shouldBe listOf("error")
    }

    @Test
    fun `error flow - timeout`() {
        every { process.waitFor(3, TimeUnit.SECONDS) } returns false

        val code = shouldThrow<InvocationException> {
            processRunner.execute(command, 3)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.TIMEOUT_EXCEPTION
    }

    companion object {
        val command = listOf("command", "args")
        val pb: ProcessBuilder = mockk<ProcessBuilder>(relaxUnitFun = true)
        val process = mockk<Process>(relaxUnitFun = true)

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            mockkObject(ProcessRunner.Companion)
            every { ProcessRunner.Companion.processBuilder(command) } returns pb
            every { pb.start() } returns process
        }
    }

}
