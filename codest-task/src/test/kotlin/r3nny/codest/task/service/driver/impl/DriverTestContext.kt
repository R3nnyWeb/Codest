package r3nny.codest.task.service.driver.impl

import io.mockk.mockkStatic
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TaskParameters
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.config.AppConfig
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.helper.readFile

abstract class DriverTestContext {

    init {
        mockkStatic(::readFile)
    }

    internal val request = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        parameters = TaskParameters(
            inputTypes = listOf(Type.INTEGER, Type.STRING, Type.BOOLEAN, Type.INTEGER_ARR, Type.STRING_ARR),
            outputType = Type.INTEGER_ARR
        ),
        startCode = mapOf(
            Language.JAVA to "some start java code",
            Language.PYTHON to "some start python code"
        ),
        tests = listOf()
    )
    internal val config = AppConfig(
        mapOf(
            Type.INTEGER to
                    AppConfig.TypeConfig(
                        readMethod = "READ_INTEGER",
                        typeConfig = mapOf(
                            Language.JAVA to AppConfig.TypeLanguageConfig(
                                read = "private static int READ_INTEGER(){ return readInteger; }",
                                typeName = "int"
                            ),
                            Language.PYTHON to AppConfig.TypeLanguageConfig(
                                read = "def READ_STRING():\n" +
                                        "    return input()",
                                typeName = "int"
                            )
                        )
                    ),
            Type.STRING to
                    AppConfig.TypeConfig(
                        readMethod = "READ_STRING",
                        typeConfig = mapOf(
                            Language.JAVA to AppConfig.TypeLanguageConfig(
                                read = "private static String READ_STRING() { return readString; }",
                                typeName = "String"
                            ),
                            Language.PYTHON to AppConfig.TypeLanguageConfig(
                                read = "def READ_STRING():\n" +
                                        "    return readString()",
                                typeName = "str"
                            )
                        )
                    ),


            Type.BOOLEAN to AppConfig.TypeConfig(
                readMethod = "READ_BOOLEAN",
                typeConfig = mapOf(
                    Language.JAVA to AppConfig.TypeLanguageConfig(
                        read = "private static boolean READ_BOOLEAN() { return readBoolean; }",
                        typeName = "boolean"
                    ),
                    Language.PYTHON to AppConfig.TypeLanguageConfig(
                        read = "def READ_BOOLEAN():\n" +
                                "    return readBoolean()",
                        typeName = "bool"
                    )
                )
            ),

            Type.INTEGER_ARR to AppConfig.TypeConfig(
                readMethod = "READ_INTEGER_ARR",
                typeConfig = mapOf(
                    Language.JAVA to AppConfig.TypeLanguageConfig(
                        read = "private static int[] READ_INTEGER_ARR() { return readIntegerArray; }",
                        typeName = "int[]"
                    ),
                    Language.PYTHON to AppConfig.TypeLanguageConfig(
                        read = "def READ_INTEGER_ARR():\n" +
                                "    return readIntegerArray()",
                        typeName = "List[int]"
                    )
                )
            ),

            Type.STRING_ARR to AppConfig.TypeConfig(
                readMethod = "READ_STRING_ARR",
                typeConfig = mapOf(
                    Language.JAVA to AppConfig.TypeLanguageConfig(
                        read = "private static String[] READ_STRING_ARR() { return readStringArray; }",
                        typeName = "String[]"
                    ),
                    Language.PYTHON to AppConfig.TypeLanguageConfig(
                        read = "def READ_STRING_ARR():\n" +
                                "    return readStringArray()",
                        typeName = "List[str]"
                    )
                )
            )
        )
    )
}