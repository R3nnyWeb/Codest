package r3nny.codest.api.service.driver.internal

import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language
import r3nny.codest.api.builder.buildGenerator
import r3nny.codest.api.service.driver.DriverTestContext


class LanguageDriverGeneratorBuilderTest : DriverTestContext() {

    @Test
    fun `success - flow`() {
        buildGenerator(Language.JAVA, config) should beInstanceOf<JavaDriverGenerator>()
        buildGenerator(Language.PYTHON, config) should beInstanceOf<PythonDriverGenerator>()
    }
}