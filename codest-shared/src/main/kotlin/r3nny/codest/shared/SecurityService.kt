package r3nny.codest.shared

import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.throwSecurityException
import r3nny.codest.shared.helper.JwtService
import ru.tinkoff.kora.common.Principal
import java.util.*

class SecurityService(
    private val jwtService: JwtService
) {

    fun exctractPrincipal(value: String?): Principal {
        val token = value
            ?.takeUnless { it.length < BEARER_PREFIX_LENGTH }
            ?.substring(BEARER_PREFIX_LENGTH)
            ?.trim()
            ?: throwSecurityException(
                code = DefaultSecurityExceptionCode.UNAUTHORIZED,
                message = "Jwt token not found"
            )

        val userId = jwtService.getUserId(token)
        return PrincipalImpl(userId)
    }

    companion object {
        private const val BEARER_PREFIX_LENGTH = 6
    }
}

data class PrincipalImpl(
    val userId: UUID
) : Principal {
    // Тут можно добавить роли
}

class NoopPrincipalImpl() : Principal