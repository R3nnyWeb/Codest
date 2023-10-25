package r3nny.codest.task.service.driver.internal

import io.kotest.matchers.should
import io.kotest.matchers.types.beInstanceOf
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.Language


class LanguageDriverGeneratorBuilderTest : DriverTestContext(){

    @Test
    fun `success - flow`(){
        LanguageDriverGenerator.buildGenerator(Language.JAVA, config) should beInstanceOf<JavaDriverGenerator>()
        LanguageDriverGenerator.buildGenerator(Language.PYTHON, config) should beInstanceOf<JavaDriverGenerator>()
    }
}