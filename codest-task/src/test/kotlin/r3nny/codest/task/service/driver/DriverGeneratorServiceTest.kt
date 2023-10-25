package r3nny.codest.task.service.driver

import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.service.driver.internal.DriverTestContext
import r3nny.codest.task.service.driver.internal.JavaDriverGenerator
import r3nny.codest.task.service.driver.internal.LanguageDriverGenerator


class DriverGeneratorServiceTest : DriverTestContext(){
    private val sut = DriverGeneratorService(config)


    @Test
    fun `success - generate driver for each language`() = runBlocking{
        mockkStatic(LanguageDriverGenerator::buildGenerator)
        coEvery {LanguageDriverGenerator.buildGenerator(Language.JAVA, any())} returns  mockk<JavaDriverGenerator>()
        val languages = Language.values()

        val drivers = sut.generate(request)

        drivers.keys shouldBe languages.toSet()
    }
}