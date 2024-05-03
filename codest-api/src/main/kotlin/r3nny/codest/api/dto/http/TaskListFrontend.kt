package r3nny.codest.api.dto.http

import r3nny.codest.api.dto.common.Level
import java.util.UUID

data class TaskListFrontend(
    val id: UUID,
    val name: String,
    val level: Level
)
