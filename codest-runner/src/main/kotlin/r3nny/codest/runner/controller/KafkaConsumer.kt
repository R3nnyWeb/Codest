package r3nny.codest.runner.controller

import kotlinx.coroutines.runBlocking
import r3nny.codest.runner.operation.RunCodeOperation
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.json.common.annotation.Json
import ru.tinkoff.kora.kafka.common.annotation.KafkaListener
import ru.tinkoff.kora.logging.common.annotation.Log
import java.util.UUID


@Component
open class KafkaConsumer(
    private val runCodeOperation: RunCodeOperation,
) {

    @KafkaListener("kafka.consumer")
    open fun process(key: UUID, @Json value: RunCodeRequestEvent ) = runBlocking {
        runCodeOperation.activate(value, key)
    }

}
