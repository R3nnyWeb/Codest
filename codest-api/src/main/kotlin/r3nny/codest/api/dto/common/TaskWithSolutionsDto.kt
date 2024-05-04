package r3nny.codest.api.dto.common

import r3nny.codest.api.dto.dao.AttemptByTaskDto
import r3nny.codest.model.Level
import r3nny.codest.model.SolutionLiteResponse
import r3nny.codest.model.TaskLiteResponse

data class TaskWithSolutionsDto(
    val task: TaskLiteDto,
    val solutions: List<AttemptByTaskDto>,
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
        solutions = solutions.toResponse()
    )

}

private fun List<AttemptByTaskDto>.toResponse(): List<SolutionLiteResponse> {
    return this.map {
        SolutionLiteResponse(
            id = it.id,
            status = it.status.api,
            createdAt = it.createdAt,
            language = it.language.name.lowercase()
        )
    }
}
