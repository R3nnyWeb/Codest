package r3nny.codest.runner.service

import java.io.File

interface CodeFileService {

    suspend fun save(code: String, fileName: String): File

    suspend fun delete(pathToFile: String)

   suspend fun read(pathToFile: String): String

}