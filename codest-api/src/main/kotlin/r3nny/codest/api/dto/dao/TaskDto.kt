package r3nny.codest.api.dto.dao

import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.dto.common.TaskInternalDto
import r3nny.codest.api.dto.common.TaskLiteDto
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.domain.Type
import java.util.*

data class TaskDto(
    val id: UUID,
    val name: String,
    val methodName: String,
    val drivers: Map<Language, String>,
    val startCode: Map<Language, String>,
    val languages: Set<Language>,
    val isEnabled: Boolean = false,
    val isPrivate: Boolean,
    val description: String,
    val level: Level,
    val inputTypes: List<Type>,
    val outputType: Type,
) {
    fun toLiteDto() = TaskLiteDto(
        id = id,
        name = name,
        level = level,
        description = description,
        languages = languages,
        isPrivate = isPrivate,
        isEnabled = isEnabled,
        startCode = startCode
    )

    fun toInternalDto(language: Language, tests: List<TestCase>) = TaskInternalDto(
        id = id,
        driver = drivers[language]!!,
        tests = tests
    )
}
