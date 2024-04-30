package r3nny.codest.task.dto.extentions

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.common.TaskParameters
import r3nny.codest.task.model.CreateTaskRequest

fun CreateTaskRequest.parameters() = TaskParameters(
    inputTypes = inputTypes.map(Type::fromString).toList(),
    outputType = Type.fromString(outputType),
)

fun CreateTaskRequest.languages() = languages?.map(Language::fromString)?.toSet() ?: Language.entries.toSet()

fun CreateTaskRequest.tests() = tests.map { TestCase(inputValues = it.inputData, outputValue = it.outputData) }
