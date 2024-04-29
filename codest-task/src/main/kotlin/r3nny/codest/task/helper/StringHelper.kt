package r3nny.codest.task.helper

import r3nny.codest.task.service.driver.internal.Key
import java.util.regex.Pattern

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