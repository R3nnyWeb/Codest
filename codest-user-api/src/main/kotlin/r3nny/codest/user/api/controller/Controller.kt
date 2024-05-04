package r3nny.codest.user.api.controller

import r3nny.codest.user.api.api.AuthApiDelegate
import r3nny.codest.user.api.api.AuthApiResponses
import r3nny.codest.user.api.api.UsersApiDelegate
import r3nny.codest.user.api.api.UsersApiResponses
import r3nny.codest.user.api.model.CreateUserRequest
import r3nny.codest.user.api.model.CreateUserResponse
import r3nny.codest.user.api.model.LoginRequest
import r3nny.codest.user.api.model.LoginResponse
import r3nny.codest.user.api.operation.CreateUserOperation
import r3nny.codest.user.api.operation.LoginOperation
import ru.tinkoff.kora.common.Component

@Component
class Controller(
    private val createUserOperation: CreateUserOperation,
    private val loginOperation: LoginOperation,
) : UsersApiDelegate, AuthApiDelegate {
    override suspend fun login(loginRequest: LoginRequest): AuthApiResponses.LoginApiResponse {
        return loginOperation.activate(loginRequest).toResponse()
    }

    override suspend fun createUser(createUserRequest: CreateUserRequest): UsersApiResponses.CreateUserApiResponse {
        return createUserOperation.activate(createUserRequest).toResponse()
    }
}

private fun LoginResponse.toResponse() = AuthApiResponses.LoginApiResponse.Login200ApiResponse(this)

private fun CreateUserResponse.toResponse()= UsersApiResponses.CreateUserApiResponse.CreateUser200ApiResponse(this)
