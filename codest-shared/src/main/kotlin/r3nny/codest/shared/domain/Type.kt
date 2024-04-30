package r3nny.codest.shared.domain

enum class Type() {
    INTEGER(),
    INTEGER_ARR(),
    STRING(),
    STRING_ARR(),
    BOOLEAN(),;

    companion object {

        val stringMap = entries.associateBy { it.name }

        fun fromString(string: String): Type = stringMap[string.uppercase()] ?: error("Неправильный тип: $string. Доступные ${stringMap.keys.map { it.lowercase() }}")
    }
}
