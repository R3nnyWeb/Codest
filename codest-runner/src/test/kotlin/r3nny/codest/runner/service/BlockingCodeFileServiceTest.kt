package r3nny.codest.runner.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.shared.exception.InvocationException

class BlockingCodeFileServiceTest {
    private val sut = BlockingCodeFileService()
    private val code = "SomeCode"
    private val fileName = "someRandom"

    @AfterEach
    fun clean() = runBlocking {
        sut.delete(fileName)
    }

    @Test
    fun save_file_then_read(): Unit = runBlocking {
        sut.save(code, fileName)

        sut.read(fileName) shouldContain code
    }

    @Test
    fun save_exists_file(): Unit = runBlocking {
        sut.save(code, fileName)
        val code = shouldThrow<InvocationException> {
            sut.save(code, fileName)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.FILE_WRITE_ERROR
    }

    @Test
    fun read_file_not_exists() = runBlocking {
        val code = shouldThrow<InvocationException> {
            sut.read(fileName)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.FILE_READ_ERROR
    }

    @Test
    fun delete_file_not_exists() = runBlocking {
        sut.delete(fileName)
    }

    @Test
    fun delete_file_exists(): Unit = runBlocking {
        sut.save(code, fileName)

        sut.delete(fileName)

        shouldThrow<InvocationException> { sut.read(fileName) }
    }

}