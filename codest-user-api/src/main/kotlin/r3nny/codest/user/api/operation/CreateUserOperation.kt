package r3nny.codest.user.api.operation

import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.user.api.exception.LogicExceptionCode
import r3nny.codest.user.api.integration.db.UserAdapter
import r3nny.codest.user.api.model.CreateUserRequest
import r3nny.codest.user.api.model.CreateUserResponse
import r3nny.codest.user.api.service.BcryptPasswordService
import java.util.*

class CreateUserOperation(
    private val userAdapter: UserAdapter,
    private val bcryptPasswordService: BcryptPasswordService,
) {

    suspend fun activate(request: CreateUserRequest): CreateUserResponse {
        val id = UUID.randomUUID()
        val password = bcryptPasswordService.encrypt(request.password)
        if (userAdapter.save(id, request.username, password)) {
            return CreateUserResponse(id)
        } else throwLogicException(
            code = LogicExceptionCode.USERNAME_ALREADY_EXISTS
        )
    }

}