package r3nny.codest.api.dto.dao

import r3nny.codest.model.SolutionLiteResponse
import r3nny.codest.shared.domain.Language
import java.time.LocalDateTime
import java.util.*

data class AttemptByTaskDto(
    val id: UUID,
    val status: StatusDto,
    val createdAt: LocalDateTime,
    val language: Language,
) {

    companion object {
        fun List<AttemptByTaskDto>.toResponse(): List<SolutionLiteResponse> {
            return this.map {
                SolutionLiteResponse(
                    id = it.id,
                    status = it.status.api,
                    createdAt = it.createdAt,
                    language = it.language.name.lowercase()
                )
            }
        }
    }
}
