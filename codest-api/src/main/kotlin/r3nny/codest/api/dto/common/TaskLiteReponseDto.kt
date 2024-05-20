package r3nny.codest.api.dto.common

import r3nny.codest.model.Level
import r3nny.codest.model.TaskLiteResponse

data class TaskLiteReponseDto(
    val task: TaskLiteDto,
    val isAuthor: Boolean,
) {
    fun toResponse() = TaskLiteResponse(
        id = task.id,
        name = task.name,
        level = Level.fromValue(task.level.name.lowercase()),
        description = task.description,
        languages = task.languages.map { it.name.lowercase() },
        startCodes = task.startCode.mapKeys { (k, v) -> k.name.lowercase() },
        isPrivate = task.isPrivate,
        isEnabled = task.isEnabled,
        isAuthor = isAuthor,
    )

}
