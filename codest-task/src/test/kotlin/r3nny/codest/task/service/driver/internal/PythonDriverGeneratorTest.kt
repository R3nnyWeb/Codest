package r3nny.codest.task.service.driver.internal

import io.mockk.coEvery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import r3nny.codest.task.helper.readFile
import r3nny.codest.task.service.driver.DriverTestContext

class PythonDriverGeneratorTest : DriverTestContext() {


    val sourceDriver = """
{{solution}}

{{readMethods}}

if __name__ == '__main__':
{paramsInputSection}}

    s = Solution()
    start_time = time.time()
    ret = Solution.{{methodName}}(s, {{paramList}})        
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        coEvery { readFile("Driver.py") } returns sourceDriver
    }

    @Test
    fun `success - unique types`(){
        val driver = sut.generate(request)

    }
}