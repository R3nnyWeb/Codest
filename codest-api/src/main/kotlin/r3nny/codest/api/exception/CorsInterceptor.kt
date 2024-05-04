package r3nny.codest.api.exception

import r3nny.codest.model.ErrorResponse
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Context
import ru.tinkoff.kora.common.Tag
import ru.tinkoff.kora.http.server.common.HttpServerInterceptor
import ru.tinkoff.kora.http.server.common.HttpServerModule
import ru.tinkoff.kora.http.server.common.HttpServerRequest
import ru.tinkoff.kora.http.server.common.HttpServerResponse
import ru.tinkoff.kora.json.common.JsonWriter
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
        return chain!!.process(context, request).whenComplete { req, _ ->
            req.headers().set("Access-Control-Allow-Origin", "*")
        }
    }
}