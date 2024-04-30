package r3nny.codest.solution.dto.http

import r3nny.codest.solution.integration.http.task.model.InternalTaskResponse


data class TaskResponseDto(
    val errorType: String?,
    val payload: InternalTaskResponse?
)
