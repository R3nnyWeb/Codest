package r3nny.codest.api.exception

import r3nny.codest.model.ErrorResponse
import r3nny.codest.shared.exception.CustomException
import r3nny.codest.shared.exception.InvocationException
import r3nny.codest.shared.exception.LogicException
import r3nny.codest.shared.exception.SecurityException
import r3nny.codest.shared.exception.ValidationException
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Context
import ru.tinkoff.kora.common.Tag
import ru.tinkoff.kora.http.common.body.HttpBody
import ru.tinkoff.kora.http.common.header.HttpHeaders
import ru.tinkoff.kora.http.server.common.HttpServerInterceptor
import ru.tinkoff.kora.http.server.common.HttpServerModule
import ru.tinkoff.kora.http.server.common.HttpServerRequest
import ru.tinkoff.kora.http.server.common.HttpServerResponse
import ru.tinkoff.kora.http.server.common.HttpServerResponseException
import ru.tinkoff.kora.json.common.JsonWriter
import java.util.concurrent.CompletionException
import java.util.concurrent.CompletionStage

@Tag(HttpServerModule::class)
@Component
class CorsInterceptor(
    val mapper: JsonWriter<ErrorResponse>,
) : HttpServerInterceptor {
    override fun intercept(
        context: Context?,
        request: HttpServerRequest?,
        chain: HttpServerInterceptor.InterceptChain?,
    ): CompletionStage<HttpServerResponse> {
        return chain!!.process(context, request)
            .exceptionally { e ->
                if (e is CompletionException) {
                    toResponse(e.cause!!)
                } else toResponse(e)
            }
            .whenComplete { req, _ ->
                req?.headers()?.set("Access-Control-Allow-Origin", "*")
            }
    }

    private fun toResponse(e: Throwable): HttpServerResponse? {
        val errorBody = when (e) {
            is IllegalArgumentException -> ErrorResponse(
                errorMessage = e.message ?: "Invalid argument", errorCode = "BadRequest"
            )

            is HttpServerResponseException -> ErrorResponse(
                errorMessage = e.message ?: "Invalid argument", errorCode = "BadRequest"
            )

            is CustomException -> ErrorResponse(
                errorMessage = e.message, errorCode = e.exceptionCode.errorCode
            )

            else -> ErrorResponse(
                errorMessage = e.message ?: "Unknown error", errorCode = "InternalError"
            )
        }

        val code = when (e) {
            is IllegalArgumentException -> 400
            is SecurityException -> if (e.isAuthorization) 403 else 401
            is LogicException -> 422
            is ValidationException -> 400
            is InvocationException -> 500
            else -> 500
        }
        e.printStackTrace()

        return HttpServerResponse.of(
            code, HttpHeaders.of(
                "Access-Control-Allow-Origin",
                "*",
                "Access-Control-Allow-Methods",
                "GET,POST,PUT,PATCH,DELETE,OPTIONS",
                "Access-Control-Allow-Headers",
                "*"
            ), HttpBody.json(mapper.toByteArray(errorBody))
        )
    }
}
