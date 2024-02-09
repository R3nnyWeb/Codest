package r3nny.codest.task.dto.http

import r3nny.codest.shared.domain.Language

data class AddLanguageToTaskRequest(
    val language: Language,
    val startCode: String
)