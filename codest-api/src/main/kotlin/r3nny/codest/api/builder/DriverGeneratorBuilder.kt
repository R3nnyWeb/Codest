package r3nny.codest.api.builder

import r3nny.codest.shared.domain.Language
import r3nny.codest.api.config.AppConfig
import r3nny.codest.api.service.driver.internal.JavaDriverGenerator
import r3nny.codest.api.service.driver.internal.LanguageDriverGenerator
import r3nny.codest.api.service.driver.internal.PythonDriverGenerator

fun buildGenerator(language: Language, config: AppConfig): LanguageDriverGenerator = when (language) {
    Language.JAVA -> JavaDriverGenerator(config)
    Language.PYTHON -> PythonDriverGenerator(config)
}