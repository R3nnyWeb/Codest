package r3nny.codest.task.service.validation

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import r3nny.codest.shared.exception.ValidationException
import r3nny.codest.task.dto.dao.Level
import r3nny.codest.task.dto.http.CreateTaskRequest

class CreateTaskValidationKtTest() {
    private val request = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        parameters = TaskParameters(
            inputTypes = listOf(Type.INTEGER, Type.INTEGER),
            outputType = Type.INTEGER_ARR
        ),
        languages = Language.values().toSet(),
        startCode = mapOf(
            Language.JAVA to "some start java code",
            Language.PYTHON to "some start python code"
        ),
        level = Level.EASY,
        tests = listOf(
            TestCase(
                inputValues = listOf("2", "2"),
                outputValue = "[2,2]"
            ),
            TestCase(
                inputValues = listOf("2", "2"),
                outputValue = "[2,2]"
            ),
        )
    )

    @Test
    fun success() {
        with(request) {
            shouldNotThrowAny {
                validateCreateTask(this)
            }
        }
    }

    @Test
    fun `error - start code not for all`() {
        with(
            request.copy(
                startCode = mapOf(
                    Language.JAVA to "some start java code",
                ),
            )
        ) {
            shouldThrow<ValidationException> {
                validateCreateTask(this)
            }
        }

    }

    @Test
    fun `error - tests less then two`() {

        with(
            request.copy(
                tests = listOf(
                    TestCase(
                        inputValues = listOf("2", "2"),
                        outputValue = "[2,2]"
                    )
                )
            )
        ) {
            shouldThrow<ValidationException> {
                validateCreateTask(this)
            }
        }

    }

    @Test
    fun `error - count of test input neq`() {
        with(
            request.copy(
                tests = listOf(
                    TestCase(
                        inputValues = listOf("2"),
                        outputValue = "[2,2]"
                    ),
                    TestCase(
                        inputValues = listOf("2", "2", "2"),
                        outputValue = "[2,2]"
                    )
                )
            )
        ) {
            shouldThrow<ValidationException> {
                validateCreateTask(this)
            }
        }
    }

    @Test
    fun `error - input params empty`() {
        with(
            request.copy(
                parameters = TaskParameters(
                    inputTypes = listOf(),
                    outputType = Type.INTEGER_ARR
                )
            )
        ) {

            shouldThrow<ValidationException> {
                validateCreateTask(this)
            }
        }
    }
}