package r3nny.codest.task.config

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type

data class AppConfig(
    val typeLanguageMapping: Map<Type, Map<Language, TypeLanguageConfig>>,
) {
    data class TypeLanguageConfig(
        val read: String,
        val typeName: String,
    )
}