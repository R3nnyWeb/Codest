package r3nny.codest.task

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import r3nny.codest.task.config.AppConfig
import ru.tinkoff.kora.application.graph.KoraApplication
import ru.tinkoff.kora.cache.caffeine.CaffeineCacheModule
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
interface Application :
    HoconConfigModule,
    CaffeineCacheModule,
    MetricsModule,
    JsonModule,
    UndertowHttpServerModule,
    OpenApiManagementModule,
    JdbcDatabaseModule,
    LogbackModule,
    LoggingModule {

    fun getConfig(): AppConfig {
        return ConfigLoaderBuilder.default()
            .addResourceSource("/config.json")
            .allowUnresolvedSubstitutions()
            .build()
            .loadConfigOrThrow<AppConfig>()
    }
}

fun main() {
    KoraApplication.run { ApplicationGraph.graph() }
}
