
import r3nny.codest.shared.logging.TestAspect
import kotlin.test.assertEquals

class Test {




    @org.junit.jupiter.api.Test
    fun test() {
        assertEquals(TestAspect().testMethod(), "42")
    }
}