package r3nny.codest.user.api.service

import at.favre.lib.crypto.bcrypt.BCrypt
import ru.tinkoff.kora.common.Component

@Component
class BcryptPasswordService {

    fun encrypt(password: String) : String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    fun isMatched(password: String, hashedPassword: String): Boolean = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified
}