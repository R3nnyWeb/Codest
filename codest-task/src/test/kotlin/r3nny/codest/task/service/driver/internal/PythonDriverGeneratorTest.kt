package r3nny.codest.task.service.driver.internal

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import r3nny.codest.task.helper.readFile
import r3nny.codest.task.service.driver.DriverTestContext

class PythonDriverGeneratorTest : DriverTestContext() {
    val sut = PythonDriverGenerator(config)

    val sourceDriver = """
{{solution}}

{{readMethods}}

if __name__ == '__main__':
{{paramsInputSection}}
    s = Solution()
    start_time = time.time()
    ret = Solution.{{methodName}}(s, {{paramsList}})
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        coEvery { readFile("Driver.py") } returns sourceDriver
    }

    @Test
    fun `success - unique types`(){
        val driver = sut.generate(request)

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
    param0 = READ_INTEGER()
    param1 = READ_STRING()
    param2 = READ_BOOLEAN()
    param3 = READ_INTEGER_ARR()
    param4 = READ_STRING_ARR()

    s = Solution()
    start_time = time.time()
    ret = Solution.method(s, param0,param1,param2,param3,param4)
        """.trimIndent()
    }
}