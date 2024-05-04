package r3nny.codest.user.api.integration.db

import r3nny.codest.user.api.dto.UserDto
import ru.tinkoff.kora.database.common.UpdateCount
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository
import java.util.*

@Repository
interface UserRepository : JdbcRepository {

    @Query("""
        INSERT INTO users (id, username, password) VALUES (:id, :username, :password)
        ON CONFLICT (username) DO NOTHING
    """)
    suspend fun save(id: UUID, username: String, password: String): UpdateCount

    @Query("SELECT id, username, password FROM users WHERE username = :username")
    suspend fun getByUsername(username: String): UserDto?
}
