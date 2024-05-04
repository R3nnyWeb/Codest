package r3nny.codest.user.api.operation

import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.throwSecurityException
import r3nny.codest.shared.helper.JwtService
import r3nny.codest.user.api.dto.UserDto
import r3nny.codest.user.api.integration.db.UserAdapter
import r3nny.codest.user.api.model.LoginRequest
import r3nny.codest.user.api.model.LoginResponse
import r3nny.codest.user.api.service.BcryptPasswordService

class LoginOperation(
    private val userAdapter: UserAdapter,
    private val bcryptPasswordService: BcryptPasswordService,
    private val jwtService: JwtService,
) {

    suspend fun activate(request: LoginRequest): LoginResponse {
        val user: UserDto = userAdapter.getByUsername(request.username) ?: throwSecurityException(
            code = DefaultSecurityExceptionCode.UNAUTHORIZED
        )
        if (!bcryptPasswordService.isMatched(request.password, user.password)) {
            throwSecurityException(
                code = DefaultSecurityExceptionCode.UNAUTHORIZED
            )
        }

        return LoginResponse(
            token = jwtService.generateToken(user.id)
        )
    }
}