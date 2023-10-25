package r3nny.codest.task.builder

import r3nny.codest.shared.domain.Language
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.service.driver.internal.JavaDriverGenerator
import r3nny.codest.task.service.driver.internal.LanguageDriverGenerator
import r3nny.codest.task.service.driver.internal.PythonDriverGenerator

fun buildGenerator(language: Language, config: AppConfig): LanguageDriverGenerator = when (language) {
    Language.JAVA -> JavaDriverGenerator(config)
    Language.PYTHON -> PythonDriverGenerator(config)
}