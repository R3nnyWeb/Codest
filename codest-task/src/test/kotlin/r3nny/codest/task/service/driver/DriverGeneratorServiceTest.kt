package r3nny.codest.task.service.driver

import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockkStatic
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.builder.buildGenerator


class DriverGeneratorServiceTest : DriverTestContext() {
    private val sut = DriverGeneratorService(config)


    @Test
    fun `success - generate driver for each language`() = runBlocking {
        mockkStatic(::buildGenerator)

        val drivers = sut.generate(request)

        val languages = Language.values()
        drivers.keys shouldBe languages.toSet()
        coVerify {
            buildGenerator(Language.JAVA, config)
            buildGenerator(Language.PYTHON, config)
        }

    }
}