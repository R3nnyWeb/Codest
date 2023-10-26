package r3nny.codest.task.service.driver.internal

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.helper.readFile
import r3nny.codest.task.service.driver.DriverTestContext

class JavaDriverGeneratorTest: DriverTestContext() {
    val sut = JavaDriverGenerator(config)
    val sourceDriver = """
        {{solution}}
        
        public class Driver{
            private static final Scanner scanner = new Scanner(System.in);
            
            public static void main(String[] args) {
                {{paramsInputSection}}
            
                {{returnType}} result = new Solution().{{methodName}}({{paramsList}});
                
                scanner.close();
            }
            
            public static void printResult(Object obj){}
            
            {{readMethods}}
        
        }
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        coEvery { readFile("Driver.java") } returns sourceDriver
    }

    @Test
    fun `success - not unique types`(){
        val driver = sut.generate(
            request.copy(
                parameters = TaskParameters(
                    outputType = Type.BOOLEAN,
                    inputTypes = listOf(Type.INTEGER,Type.INTEGER,Type.INTEGER)
                )
            )
        )

        driver shouldBe """
        {{solution}}
        
        public class Driver{
            private static final Scanner scanner = new Scanner(System.in);
            
            public static void main(String[] args) {
                var param0 = READ_INTEGER();
        var param1 = READ_INTEGER();
        var param2 = READ_INTEGER();
        
            
                boolean result = new Solution().method(param0,param1,param2);
                
                scanner.close();
            }
            
            public static void printResult(Object obj){}
            
            private static int READ_INTEGER(){ return readInteger; }
        
        
        }
        """.trimIndent()
    }

    //success - not unique types
    @Test
    fun `success - unique types`(){
        val driver = sut.generate(request)

        driver shouldBe """
        {{solution}}
        
        public class Driver{
            private static final Scanner scanner = new Scanner(System.in);
            
            public static void main(String[] args) {
                var param0 = READ_INTEGER();
        var param1 = READ_STRING();
        var param2 = READ_BOOLEAN();
        var param3 = READ_INTEGER_ARR();
        var param4 = READ_STRING_ARR();
        
            
                int[] result = new Solution().method(param0,param1,param2,param3,param4);
                
                scanner.close();
            }
            
            public static void printResult(Object obj){}
            
            private static int READ_INTEGER(){ return readInteger; }
        private static String READ_STRING() { return readString; }
        private static boolean READ_BOOLEAN() { return readBoolean; }
        private static int[] READ_INTEGER_ARR() { return readIntegerArray; }
        private static String[] READ_STRING_ARR() { return readStringArray; }
        
        
        }
    """.trimIndent()

    }

}