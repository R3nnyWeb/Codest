package r3nny.codest.runner.service

interface CodeFileService {

    suspend fun save(code: String, extention: String): String

    suspend fun delete(pathToFile: String)

   suspend fun read(pathToFile: String): String

}