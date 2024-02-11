package r3nny.codest.runner.service

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.helper.wrap
import java.io.File

class BlockingCodeFileService : CodeFileService {

    @LogMethod
    override suspend fun save(code: String, fileName: String) {
        wrap(
            code = InvocationExceptionCode.FILE_WRITE_ERROR
        ) {
            val file = File(fileName)
            if (file.exists()) throw Exception("File already exists")

            file.printWriter().use {
                it.println(code)
            }
        }
    }

    @LogMethod
    override suspend fun delete(fileName: String) {
        File(fileName).delete()
    }

    @LogMethod
    override suspend fun read(fileName: String) =
        wrap(
            code = InvocationExceptionCode.FILE_READ_ERROR
        ) {
            val file = File(fileName)
            if (!file.exists()) throw Exception("File not exists")

            file.readText()
        }

}
