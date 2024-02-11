package r3nny.codest.runner.config

import r3nny.codest.shared.dto.runner.ExecutableLanguage

data class AppConfig(
    val logic: Logic
)

data class Logic(
    val languageSettings: Map<ExecutableLanguage, LanguageSettings>
)

data class LanguageSettings(
    val commandToCompile: String?,
    val argsToCompile: String?,
    val commandToRun: String,
    val argsToRun: String?,
    val extension: String
)