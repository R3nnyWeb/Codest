package r3nny.codest.api.dto.common

import r3nny.codest.shared.domain.TestCase
import java.util.*

class TaskInternalDto(
    val id: UUID,
    val driver: String,
    val tests: List<TestCase>
)
