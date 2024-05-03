package r3nny.codest.api.helper

import com.sksamuel.hoplite.ConfigException
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.api.config.AppConfig

fun AppConfig.getTypeLanguageConfig(type: Type, language: Language) : AppConfig.TypeLanguageConfig {
    val typeMap = getTypeConfig(type)
    return typeMap.typeConfig[language] ?: throw ConfigException("Language $language on Type $type not exist on config")
}

fun AppConfig.getTypeConfig(type: Type): AppConfig.TypeConfig =
    typeLanguageMapping[type] ?: throw ConfigException("Type $type not exist on config")

fun AppConfig.getDriver(language: Language): String =
    drivers[language] ?: throw ConfigException("Language $language not exist on config")