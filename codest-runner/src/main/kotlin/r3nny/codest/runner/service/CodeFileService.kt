package r3nny.codest.runner.service

interface CodeFileService {

    suspend fun save(code: String, fileName: String)

    suspend fun delete(fileName: String)

    suspend fun read(fileName: String): String

}