package r3nny.codest.runner.config

import r3nny.codest.shared.dto.runner.ExecutableLanguage
import ru.tinkoff.kora.config.common.annotation.ConfigSource

@ConfigSource("logic")
interface LogicConfigMapping {
    fun languageSettings(): Map<String, LanguageSettings>
    fun maxTime(): Long
}

data class Logic(
    val languageSettings: Map<ExecutableLanguage, LanguageSettings>,
    val maxTime: Long,
)

data class LanguageSettings(
    val commandToCompile: String?,
    val commandToRun: String,
)
