package r3nny.codest.api.service.driver

import r3nny.codest.model.CreateTaskRequest
import r3nny.codest.model.Level
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.api.config.AppConfig

abstract class DriverTestContext {

    internal val request = CreateTaskRequest(
        name = "task",
        description = "some md descr",
        methodName = "method",
        inputTypes = listOf(Type.INTEGER, Type.STRING, Type.BOOLEAN, Type.INTEGER_ARR, Type.STRING_ARR).map { it.name },
        outputType = Type.INTEGER_ARR.name,
        startCodes = mapOf(
            Language.JAVA.name to "some start java code",
            Language.PYTHON.name to "some start python code"
        ),
        level = Level.EASY,
        tests = listOf()
    )
    internal val config = AppConfig(
        drivers = mapOf(
            Language.JAVA to "someDriver",
            Language.PYTHON to "someDriver"
        ),
        typeLanguageMapping = mapOf(
            Type.INTEGER to
                    AppConfig.TypeConfig(
                        readMethod = "READ_INTEGER",
                        typeConfig = mapOf(
                            Language.JAVA to AppConfig.TypeLanguageConfig(
                                read = "private static int READ_INTEGER(){ return readInteger; }",
                                typeName = "int"
                            ),
                            Language.PYTHON to AppConfig.TypeLanguageConfig(
                                read = "def READ_INTEGER():\n" +
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