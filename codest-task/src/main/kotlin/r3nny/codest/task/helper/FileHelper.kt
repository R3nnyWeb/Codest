package r3nny.codest.task.helper

import java.io.File

fun readFile(fileName: String): String
  = File(fileName).readText(Charsets.UTF_8)

fun writeFile(content: String, filename: String) {
    File(filename).writeText(content)
}