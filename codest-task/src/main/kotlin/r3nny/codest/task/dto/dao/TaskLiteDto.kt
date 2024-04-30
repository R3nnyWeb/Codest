package r3nny.codest.task.dto.dao

import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.common.Level
import java.util.*

data class TaskLiteDto(
    val id: UUID,
    val name: String,
    val level: Level,
    val description: String,
    val languages: Set<Language>,
    val isEnabled: Boolean,
    val isPrivate: Boolean,
    val startCode: Map<Language, String>
)
