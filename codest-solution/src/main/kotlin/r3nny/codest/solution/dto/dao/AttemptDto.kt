package r3nny.codest.solution.dto.dao

import r3nny.codest.shared.domain.Language
import java.time.LocalDateTime
import java.util.*

data class AttemptDto(
    val id: UUID,
    val status: StatusDto,
    val code: String,
    val error: String?,
    val language: Language,
    val createdAt: LocalDateTime
)
