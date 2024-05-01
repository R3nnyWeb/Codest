package r3nny.codest.shared.domain

import ru.tinkoff.kora.json.common.annotation.Json

@Json
enum class Language() {
    JAVA,
    PYTHON;

    companion object {

        val stringMap = Language.entries.associateBy { it.name }

        fun fromString(string: String): Language = stringMap[string.uppercase()]
            ?: error("Неправильный язык: $string. Доступные ${stringMap.keys.map { it.lowercase() }}")
    }
}