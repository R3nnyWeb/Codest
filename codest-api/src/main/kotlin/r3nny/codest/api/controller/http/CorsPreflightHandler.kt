package r3nny.codest.api.controller.http

import ru.tinkoff.kora.http.common.HttpMethod
import ru.tinkoff.kora.http.common.annotation.HttpRoute
import ru.tinkoff.kora.http.common.header.HttpHeaders
import ru.tinkoff.kora.http.server.common.HttpServerResponse
import ru.tinkoff.kora.http.server.common.annotation.HttpController

@HttpController
class CorsPreflightHandler {

    @HttpRoute(method = HttpMethod.OPTIONS, path = "/*")
    fun preflight() = HttpServerResponse.of(200, HttpHeaders.of("Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS", "Access-Control-Allow-Headers", "*"))
}