package r3nny.codest.api.dto.common

import r3nny.codest.shared.domain.Type

data class TaskParameters(
    val inputTypes: List<Type>,
    val outputType: Type,
)
