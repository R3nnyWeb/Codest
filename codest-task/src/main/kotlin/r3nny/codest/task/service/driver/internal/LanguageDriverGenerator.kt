package r3nny.codest.task.service.driver.internal

import r3nny.codest.task.dto.http.CreateTaskRequest
import java.util.regex.Pattern
//todo: подумоть
interface LanguageDriverGenerator {
    fun generate(request: CreateTaskRequest): String

    fun String.replaceKeysWithValues(replacements: Map<Key, String>): String {
        val pattern = Pattern.compile("\\{\\{([^}]+)}}")
        val matcher = pattern.matcher(this)
        val result = StringBuilder()
        var previousEnd = 0

        while (matcher.find()) {
            val currentKey = matcher.group(1)
            val replacement = replacements[Key.fromDriverKey(currentKey)]
            if (replacement != null) {
                result.append(this.substring(previousEnd, matcher.start()))
                result.append(replacement)
                previousEnd = matcher.end()
            }
        }

        result.append(this.substring(previousEnd))
        return result.toString()
    }

}

enum class Key(val driverKey: String) {
    SOLUTION("solution"),
    INPUT_PARAMS_READ_SECTION("paramsInputSection"),
    RETURN_TYPE("returnType"),
    METHOD_NAME("methodName"),
    INPUT_PARAMS_LIST("paramsList"),
    READ_METHODS("readMethods");

    companion object {
        fun fromDriverKey(currentKey: String?): Key? {
            val map = values().associateBy(Key::driverKey)
            return map[currentKey]
        }
    }
}

