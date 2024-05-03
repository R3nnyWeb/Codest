package r3nny.codest.api.dto.dao

import r3nny.codest.shared.domain.Language
import java.time.LocalDateTime
import java.util.*

data class AttemptByTaskDto(
    val id: UUID,
    val status: StatusDto,
    val createdAt: LocalDateTime,
    val language: Language
)