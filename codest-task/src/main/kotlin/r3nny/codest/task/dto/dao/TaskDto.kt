package r3nny.codest.task.dto.dao

import java.util.UUID
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.Type
import r3nny.codest.task.dto.common.Level

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
)
