package r3nny.codest.task.service.driver.internal

import r3nny.codest.shared.domain.Type
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.helper.readFile

class PythonDriverGenerator(
    private val config: AppConfig
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

    private fun getParamsInputSectionStr(inputTypes: List<Type>): String {
        TODO("Not yet implemented")
    }

    private fun getParamsInputListStr(size: Int): String {
        TODO("Not yet implemented")
    }

    private fun getReadMethodsStr(inputTypes: List<Type>): String {
        TODO("Not yet implemented")
    }
}