package r3nny.codest.task.integration.mongo.criteria

import r3nny.codest.task.dto.dao.Level

data class TaskSearchQuery(
    val name: String,
    val level: Level,
    val enabled: Boolean
)