package r3nny.codest.api.service.validation

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import org.junit.jupiter.api.Test
import r3nny.codest.model.CreateTaskRequest
import r3nny.codest.model.CreateTest
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.shared.exception.ValidationException

class CreateTaskValidationKtTest {
    private val request = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        inputTypes = listOf(Type.INTEGER.name, Type.INTEGER.name),
        outputType = Type.INTEGER_ARR.name,
        languages = Language.values().map { it.name },
        startCodes = mapOf(
            Language.JAVA.name to "some start java code",
            Language.PYTHON.name to "some start python code"
        ),
        level = r3nny.codest.model.Level.EASY,
        tests = listOf(
            CreateTest(
                inputData = listOf("2", "2"),
                outputData = "[2,2]"
            ),
            CreateTest(
                inputData = listOf("2", "2"),
                outputData = "[2,2]"
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
                startCodes = mapOf(
                    Language.JAVA.name to "some start java code",
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
                    CreateTest(
                        inputData = listOf("2", "2"),
                        outputData = "[2,2]"
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
                    CreateTest(
                        inputData = listOf("2"),
                        outputData = "[2,2]"
                    ),
                    CreateTest(
                        inputData = listOf("2", "2", "2"),
                        outputData = "[2,2]"
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
                inputTypes = listOf(),
                outputType = Type.INTEGER_ARR.name
            )
        ) {

            shouldThrow<ValidationException> {
                validateCreateTask(this)
            }
        }
    }
}