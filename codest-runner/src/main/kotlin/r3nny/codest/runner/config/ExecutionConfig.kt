package r3nny.codest.runner.config

import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.config.common.annotation.ConfigSource

@ConfigSource("logic")
interface LogicConfigMapping {
    fun languageSettings(): Map<String, LanguageSettings>
    fun maxTime(): Long
}

data class Logic(
    val languageSettings: Map<Language, LanguageSettings>,
    val maxTime: Long,
)

data class LanguageSettings(
    val commandToCompile: String?,
    val commandToRun: String,
)
