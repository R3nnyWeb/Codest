package r3nny.codest.task.service.driver.internal

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.helper.getDriver
import r3nny.codest.task.helper.getTypeLanguageConfig
import r3nny.codest.task.helper.replaceKeysWithValues
import ru.tinkoff.kora.logging.common.annotation.Log

open class LanguageDriverGenerator(
    val language: Language,
    val config: AppConfig,
) {

    @Log
    open fun generate(methodName: String, parameters: TaskParameters): String {

        val source = config.getDriver(language)

        return source.replaceKeysWithValues(
            mapOf(
                Key.METHOD_NAME to methodName,
                Key.RETURN_TYPE to getReturnTypeStr(parameters.outputType),
                Key.INPUT_PARAMS_READ_SECTION to getParamsInputSectionStr(parameters.inputTypes),
                Key.INPUT_PARAMS_LIST to getParamsInputListStr(parameters.inputTypes.size),
                Key.READ_METHODS to getReadMethodsStr(parameters.inputTypes)
            )
        )
    }

    protected open fun getParamsInputSectionStr(inputTypes: List<Type>): String {
        throw NotImplementedError("Not implemented")
    }

    private fun getParamsInputListStr(size: Int): String =
        with(StringBuilder()) {
            var params = mutableListOf<String>();
            for (i in 0 until size) {
                params += "param$i"
            }
            append(params.joinToString(separator = ","))
            toString()
        }

    private fun getReadMethodsStr(inputTypes: List<Type>): String =
        with(StringBuilder()) {
            for (type in inputTypes.distinctBy { it }) {
                append(config.getTypeLanguageConfig(type, language).read + "\n")
            }
            toString()
        }

    private fun getReturnTypeStr(param: Type): String =
        config.getTypeLanguageConfig(param, language).typeName


}

enum class Key(val driverKey: String) {
    SOLUTION("solution"),
    INPUT_PARAMS_READ_SECTION("paramsInputSection"),
    RETURN_TYPE("returnType"),
    METHOD_NAME("methodName"),
    INPUT_PARAMS_LIST("paramsList"),
    READ_METHODS("readMethods");

    companion object {
        fun fromDriverKey(currentKey: String?): Key? {
            val map = values().associateBy(Key::driverKey)
            return map[currentKey]
        }
    }
}

