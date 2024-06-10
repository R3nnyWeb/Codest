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
    override suspend fun save(code: String, fileName: String): File {
        return wrap(
            code = InvocationExceptionCode.FILE_WRITE_ERROR
        ) {
            val tempFolder = Files.createTempDirectory("code")
            val tempFile = File(tempFolder.toFile(), fileName)
            tempFile.printWriter().use {
                it.println(code)
            }
            tempFile
        }
    }

    @Log
    override suspend fun deleteFolder(folder: File) {
        wrap(
            code = InvocationExceptionCode.FILE_DELETE_ERROR
        ) {
//            if (folder.exists() && folder.isDirectory()) {
//                folder.listFiles()?.forEach {
//                    if (it.isFile)
//                        it.delete()
//                }
//            } else throw Exception("Directory not exists or not directory")
//            folder.delete();
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
