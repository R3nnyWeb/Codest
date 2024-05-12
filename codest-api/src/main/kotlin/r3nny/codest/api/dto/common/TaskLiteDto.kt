package r3nny.codest.api.dto.common

import r3nny.codest.model.TaskLiteResponse
import r3nny.codest.shared.domain.Language
import java.util.*

data class TaskLiteDto(
    val id: UUID,
    val name: String,
    val level: Level,
    val description: String,
    val languages: Set<Language>,
    val isEnabled: Boolean,
    val isPrivate: Boolean,
    val startCode: Map<Language, String>,
) {
    fun toResponse() = TaskLiteResponse(
        id = id,
        languages = languages.map
        { it.name.lowercase() },
        level = r3nny.codest.model.Level.valueOf(level.name.uppercase()),
        name = name,
        isEnabled = isEnabled,
        isPrivate = isPrivate,
        description = description,
        startCodes = startCode.mapKeys
        { (k, v) -> k.name.lowercase() },
        isAuthor = false
    )


}
