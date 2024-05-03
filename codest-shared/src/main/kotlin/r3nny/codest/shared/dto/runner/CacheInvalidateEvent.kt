package r3nny.codest.shared.dto.runner

import ru.tinkoff.kora.json.common.annotation.Json
import java.util.*

@Json
data class CacheInvalidateEvent(
    val taskId: UUID? = null,
    val solutionId: UUID? = null,
)
