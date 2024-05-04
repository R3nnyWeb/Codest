package r3nny.codest.api.config

import ru.tinkoff.kora.config.common.annotation.ConfigSource

@ConfigSource("security")
interface SecurityConfig {

    fun key(): String
}