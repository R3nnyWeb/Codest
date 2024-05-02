package r3nny.codest.api.dto.dao

import r3nny.codest.shared.domain.Language
import java.time.LocalDateTime
import java.util.*

data class AttemptDto(
    val id: UUID,
    val status: StatusDto,
    val taskId: UUID,
    val code: String,
    val error: List<String>?,
    val language: Language,
    val createdAt: LocalDateTime
)
