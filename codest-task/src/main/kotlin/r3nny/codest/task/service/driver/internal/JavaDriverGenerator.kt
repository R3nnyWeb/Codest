package r3nny.codest.task.service.driver.internal

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.helper.getTypeConfig

class JavaDriverGenerator(
    config: AppConfig,
) : LanguageDriverGenerator(Language.JAVA, config) {

    override fun getParamsInputSectionStr(inputTypes: List<Type>): String =
        with(StringBuilder()) {
            for ((i, type) in inputTypes.withIndex()) {
                val str = "var param$i = ${config.getTypeConfig(type).readMethod}();\n"
                append(str)
            }
            toString()
        }
}





