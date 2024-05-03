package r3nny.codest.api.controller.kafka

import kotlinx.coroutines.runBlocking
import r3nny.codest.api.logic.kafka.CacheInvalidateOperation
import r3nny.codest.api.logic.kafka.HandleRunCodeResponseOperation
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.json.common.annotation.Json
import ru.tinkoff.kora.kafka.common.annotation.KafkaListener
import java.util.*

@Component
open class KafkaController(
    private val cacheInvalidateOperation: CacheInvalidateOperation,
    private val handleRunCodeResponseOperation: HandleRunCodeResponseOperation,
) {

    @KafkaListener("kafka.cacheInvalidateConsumer")
    open fun process(key: UUID, @Json value: CacheInvalidateEvent) = runBlocking {
        cacheInvalidateOperation.activate(value)
    }

    @KafkaListener("kafka.runConsumer")
    open fun processRun(key: UUID, @Json value: RunCodeResponseEvent) = runBlocking {
        handleRunCodeResponseOperation.activate(key, value)
    }
}