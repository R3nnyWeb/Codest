package r3nny.codest.runner.service

import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.helper.wrap
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.io.File
import java.nio.file.Files

@Component
open class BlockingCodeFileService : CodeFileService {

    @Log
    override suspend fun save(code: String, extention: String): String {
        return wrap(
            code = InvocationExceptionCode.FILE_WRITE_ERROR
        ) {
            val tempFile = Files.createTempFile("code", ".$extention")
                .toFile()
            tempFile.printWriter().use {
                it.println(code)
            }
            tempFile.absolutePath
        }
    }

    @Log
    override suspend fun delete(pathToFile: String) {
        wrap(
            code = InvocationExceptionCode.FILE_DELETE_ERROR
        ) {
            val file = File(pathToFile)
            if (!file.exists()) {
                throw Exception("File does not exist")
            }
            file.delete()
        }
    }

    @Log
    override suspend fun read(pathToFile: String) =
        wrap(
            code = InvocationExceptionCode.FILE_READ_ERROR
        ) {
            val file = File(pathToFile)
            if (!file.exists()) throw Exception("File not exists")

            file.readText()
        }

}
