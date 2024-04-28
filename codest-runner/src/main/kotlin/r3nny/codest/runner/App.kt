package r3nny.codest.runner

import r3nny.codest.runner.config.Logic
import r3nny.codest.runner.config.LogicConfigMapping
import r3nny.codest.shared.dto.runner.ExecutableLanguage
import ru.tinkoff.kora.application.graph.KoraApplication
import ru.tinkoff.kora.common.KoraApp
import ru.tinkoff.kora.config.hocon.HoconConfigModule
import ru.tinkoff.kora.http.server.undertow.UndertowHttpServerModule
import ru.tinkoff.kora.json.module.JsonModule
import ru.tinkoff.kora.kafka.common.KafkaModule
import ru.tinkoff.kora.logging.common.LoggingModule
import ru.tinkoff.kora.logging.logback.LogbackModule
import ru.tinkoff.kora.micrometer.module.MetricsModule

@KoraApp
interface App :
    UndertowHttpServerModule,
    MetricsModule,
    JsonModule,
    LogbackModule,
    LoggingModule,
    HoconConfigModule,
    KafkaModule {

    fun logicConfig(config: LogicConfigMapping): Logic = Logic(
        languageSettings = config.languageSettings().mapKeys { (key, _) -> ExecutableLanguage.valueOf(key) },
        maxTime = config.maxTime()
    )

}

fun main() {
    KoraApplication.run { AppGraph.graph() }
}
