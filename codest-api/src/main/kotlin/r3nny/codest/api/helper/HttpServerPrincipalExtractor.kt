package r3nny.codest.api.helper

import r3nny.codest.api.ApiSecurity
import r3nny.codest.shared.SecurityService
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Principal
import ru.tinkoff.kora.common.Tag
import ru.tinkoff.kora.http.server.common.HttpServerRequest
import ru.tinkoff.kora.http.server.common.auth.HttpServerPrincipalExtractor
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

@Tag(ApiSecurity.BearerAuth::class)
@Component
class HttpServerPrincipalExtractor(
    private val securityService: SecurityService
): HttpServerPrincipalExtractor<Principal> {
    override fun extract(request: HttpServerRequest?, value: String?): CompletionStage<Principal> {
        return CompletableFuture.completedFuture(securityService.exctractPrincipal(value))
    }
}