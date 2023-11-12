package r3nny.codest.shared.dto

import java.time.LocalDateTime

data class ErrorDto(
    val errorMessage: String,
    val statusCode: Int,
    val timeStamp: LocalDateTime = LocalDateTime.now(),
)
