package r3nny.codest.shared.domain

import java.util.UUID

data class TestCase(
    val id: UUID = UUID.randomUUID(),
    val inputValues: List<String>,
    val outputValue: String
)
