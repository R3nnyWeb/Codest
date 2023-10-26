package r3nny.codest.task.service.driver.internal

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.helper.getTypeConfig
import r3nny.codest.task.helper.getTypeLanguageConfig
import r3nny.codest.task.helper.readFile

class PythonDriverGenerator(
    private val config: AppConfig,
) : LanguageDriverGenerator {

    override fun generate(request: CreateTaskRequest): String {
        val source = readFile("Driver.py")

        val parameters = request.parameters
        return source.replaceKeysWithValues(
            mapOf(
                Key.METHOD_NAME to request.methodName,
                Key.INPUT_PARAMS_READ_SECTION to getParamsInputSectionStr(parameters.inputTypes),
                Key.INPUT_PARAMS_LIST to getParamsInputListStr(parameters.inputTypes.size),
                Key.READ_METHODS to getReadMethodsStr(parameters.inputTypes)
            )
        )
    }

    private fun getParamsInputSectionStr(inputTypes: List<Type>): String =
        with(StringBuilder()) {
            for ((i, type) in inputTypes.withIndex()) {
                val str = "    param$i = ${config.getTypeConfig(type).readMethod}()\n"
                append(str)
            }
            toString()
        }

    //todo: to parent
    private fun getParamsInputListStr(size: Int): String =
        with(StringBuilder()) {
            var params = mutableListOf<String>();
            for (i in 0 until size) {
                params += "param$i"
            }
            append(params.joinToString(separator = ","))
            toString()
        }
    //todo: to parent
    private fun getReadMethodsStr(inputTypes: List<Type>): String =
        with(StringBuilder()) {
            for (type in inputTypes.distinctBy { it }) {
                append(config.getTypeLanguageConfig(type, Language.PYTHON).read + "\n")
            }
            toString()
        }
}