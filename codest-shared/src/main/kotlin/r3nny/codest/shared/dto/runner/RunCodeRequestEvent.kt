package r3nny.codest.shared.dto.runner

import r3nny.codest.shared.domain.Language
import ru.tinkoff.kora.json.common.annotation.Json

@Json
data class RunCodeRequestEvent(
    val code : String,
    val input: List<String>?,
    val language: Language
)
