package r3nny.codest.task.service.driver.impl

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.helper.getTypeConfig
import r3nny.codest.task.helper.getTypeLanguageConfig
import r3nny.codest.task.helper.readFile
import r3nny.codest.task.service.driver.DriverGenerator
import r3nny.codest.task.service.driver.Key

class JavaDriverGenerator(
    private val config: AppConfig,
) : DriverGenerator {
    override fun generate(request: CreateTaskRequest): String {
        val source = readFile("Driver.java")

        val parameters = request.parameters
        return source.replaceKeysWithValues(
            mapOf(
                Key.METHOD_NAME to request.methodName,
                Key.RETURN_TYPE to getReturnTypeStr(parameters.outputType),
                Key.INPUT_PARAMS_READ to getParamsInputSectionStr(parameters.inputTypes),
                Key.INPUT_PARAMS_LIST to getParamsInputListStr(parameters.inputTypes.size),
                Key.READ_METHODS to getReadMethodsStr(parameters.inputTypes)
            )
        )
    }

    private fun getReadMethodsStr(inputTypes: List<Type>): String =
        with(StringBuilder()) {
            for (type in inputTypes.distinctBy { it }) {
                append(config.getTypeLanguageConfig(type, Language.JAVA).read + "\n")
            }
            toString()
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

    private fun getReturnTypeStr(param: Type): String =
        config.getTypeLanguageConfig(param, Language.JAVA).typeName

    private fun getParamsInputSectionStr(parameters: List<Type>): String =
        with(StringBuilder()) {
            for ((i, type) in parameters.withIndex()) {
                val str = "var param$i = ${config.getTypeConfig(type).readMethod}();\n"
                append(str)
            }
            toString()
        }
}





