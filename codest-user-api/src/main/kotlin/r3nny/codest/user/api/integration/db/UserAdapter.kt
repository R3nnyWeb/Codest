package r3nny.codest.user.api.integration.db

import r3nny.codest.user.api.dto.UserDto
import java.util.*

class UserAdapter(
    private val userRepository: UserRepository,
) {

    suspend fun save(id: UUID, username: String, password: String): Boolean {
        return userRepository.save(id, username, password).value == 1L
    }

    suspend fun getByUsername(username: String): UserDto? {
        return userRepository.getByUsername(username)
    }

}
