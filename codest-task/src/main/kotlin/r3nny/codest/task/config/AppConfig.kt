package r3nny.codest.task.config

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.springframework.context.annotation.Bean
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type

data class AppConfig(
    val typeLanguageMapping: Map<Type, TypeConfig>,
) {
    data class TypeConfig(
        val readMethod: String,
        val typeConfig: Map<Language, TypeLanguageConfig>
    )
    data class TypeLanguageConfig(
        val read: String,
        val typeName: String,
    )
}
