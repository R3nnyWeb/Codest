package r3nny.codest.user.api.dto

import java.util.*

data class UserDto(
    val username: String,
    val password: String,
    val id: UUID
)
