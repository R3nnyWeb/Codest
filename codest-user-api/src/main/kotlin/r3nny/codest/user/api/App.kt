package r3nny.codest.user.api

import r3nny.codest.shared.helper.JwtService
import r3nny.codest.user.api.config.SecurityConfig
import ru.tinkoff.kora.application.graph.KoraApplication
import ru.tinkoff.kora.common.KoraApp
import ru.tinkoff.kora.config.hocon.HoconConfigModule
import ru.tinkoff.kora.database.jdbc.JdbcDatabaseModule
import ru.tinkoff.kora.http.server.undertow.UndertowHttpServerModule
import ru.tinkoff.kora.json.module.JsonModule
import ru.tinkoff.kora.logging.common.LoggingModule
import ru.tinkoff.kora.logging.logback.LogbackModule
import ru.tinkoff.kora.micrometer.module.MetricsModule
import ru.tinkoff.kora.openapi.management.OpenApiManagementModule

@KoraApp
interface App :
    UndertowHttpServerModule,
    MetricsModule,
    JsonModule,
    LogbackModule,
    LoggingModule,
    HoconConfigModule,
    OpenApiManagementModule,
    JdbcDatabaseModule {
    fun jwtService(config: SecurityConfig) = JwtService(config.key())

}


fun main() {
    KoraApplication.run { AppGraph.graph() }
}
