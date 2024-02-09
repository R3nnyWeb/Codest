package r3nny.codest.task.dto.http

import r3nny.codest.task.dto.dao.Level
import java.util.UUID

data class TaskListFrontend(
    val id: UUID,
    val name: String,
    val level: Level
)
