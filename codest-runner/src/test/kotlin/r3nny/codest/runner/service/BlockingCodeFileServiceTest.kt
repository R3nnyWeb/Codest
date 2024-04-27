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
    private val extention = "java"

    @Test
    fun save_file_then_read(): Unit = runBlocking {
        val path = sut.save(code, extention)

        sut.read(path) shouldContain code

        sut.delete(path)
    }

    @Test
    fun read_file_not_exists() = runBlocking {
        val code = shouldThrow<InvocationException> {
            sut.read(extention)
        }.exceptionCode

        code shouldBe InvocationExceptionCode.FILE_READ_ERROR
    }

    @Test
    fun delete_file_not_exists() = runBlocking {
        val code = shouldThrow<InvocationException> {
            sut.delete("some")
        }.exceptionCode

        code shouldBe InvocationExceptionCode.FILE_DELETE_ERROR
    }

    @Test
    fun delete_file_exists(): Unit = runBlocking {
        val path = sut.save(code, extention)

        sut.delete(path)

        shouldThrow<InvocationException> { sut.read(path) }
    }

}
