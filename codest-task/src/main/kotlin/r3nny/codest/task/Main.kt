package r3nny.codest.task

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.OutputStreamAppender
import ch.qos.logback.core.encoder.LayoutWrappingEncoder
import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.slf4j.LoggerFactory
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
    LogbackModule,
    CaffeineCacheModule,
    MetricsModule,
    JsonModule,
    JdbcDatabaseModule,
    UndertowHttpServerModule,
    OpenApiManagementModule
     {

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
