package r3nny.codest.api.dto.dao

import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.json.common.annotation.Json
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
