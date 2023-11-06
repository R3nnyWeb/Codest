package r3nny.codest.task.config

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type

data class AppConfig(
    val typeLanguageMapping: Map<Type, TypeConfig>,
    val drivers: Map<Language, String>,
) {
    data class TypeConfig(
        val readMethod: String,
        val typeConfig: Map<Language, TypeLanguageConfig>,
    )

    data class TypeLanguageConfig(
        val read: String,
        val typeName: String,
    )
}