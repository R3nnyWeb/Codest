package r3nny.codest.api.service.driver.internal

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import r3nny.codest.api.dto.extentions.parameters
import r3nny.codest.api.service.driver.DriverTestContext
import r3nny.codest.shared.domain.Language

class PythonDriverGeneratorTest : DriverTestContext() {


    val sourceDriver = """
{{solution}}

{{readMethods}}

if __name__ == '__main__':
    for i in range({{testsCount}}):
{{paramsInputSection}}
        s = Solution()
        ret = Solution.{{methodName}}(s, {{paramsList}})
        print(ret)
    """.trimIndent()

    val sut = PythonDriverGenerator(config.copy(
        drivers = mapOf(
            Language.PYTHON to sourceDriver
        )
    ))

    @Test
    fun `success - unique types`() {
        val driver = sut.generate(request.methodName, request.parameters())

        driver shouldBe """
{{solution}}

def READ_INTEGER():
    return input()
def READ_STRING():
    return readString()
def READ_BOOLEAN():
    return readBoolean()
def READ_INTEGER_ARR():
    return readIntegerArray()
def READ_STRING_ARR():
    return readStringArray()


if __name__ == '__main__':
    for i in range({{testsCount}}):
        param0 = READ_INTEGER()
        param1 = READ_STRING()
        param2 = READ_BOOLEAN()
        param3 = READ_INTEGER_ARR()
        param4 = READ_STRING_ARR()

        s = Solution()
        ret = Solution.method(s, param0,param1,param2,param3,param4)
        print(ret)
        """.trimIndent()
    }
}