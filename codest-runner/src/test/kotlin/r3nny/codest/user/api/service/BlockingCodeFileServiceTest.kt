package r3nny.codest.user.api.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.service.BlockingCodeFileService
import r3nny.codest.shared.exception.InvocationException
import java.io.File

class BlockingCodeFileServiceTest {
    private val sut = BlockingCodeFileService()
    private val code = "SomeCode"
    private val fileName = "java"

    @Test
    fun save_file_then_read(): Unit = runBlocking {
        val path = sut.save(code, fileName)

        sut.read(path.absolutePath) shouldContain code

        sut.deleteFolder(path.parentFile)
    }

    @Test
    fun read_file_not_exists() = runBlocking {
        val code = shouldThrow<InvocationException> {
            sut.read("any")
        }.exceptionCode

        code shouldBe InvocationExceptionCode.FILE_READ_ERROR
    }

    @Test
    fun delete_folder_not_exists() = runBlocking {
        val code = shouldThrow<InvocationException> {
            sut.deleteFolder(File("any"))
        }.exceptionCode

        code shouldBe InvocationExceptionCode.FILE_DELETE_ERROR
    }

    @Test
    fun delete_file_exists(): Unit = runBlocking {
        val path = sut.save(code, fileName)

        sut.deleteFolder(path.parentFile)

        shouldThrow<InvocationException> { sut.read(path.absolutePath) }
    }

}
