package r3nny.codest.api.dto.common

enum class Level {
    EASY,
    MEDIUM,
    HARD,
    ;

    companion object {

        val stringMap = Level.entries.associateBy { it.name }

        fun fromString(string: String): Level = stringMap[string.uppercase()]
            ?: error("Неправильный тип: $string. Доступные ${stringMap.keys.map { it.lowercase() }}")
    }
}
