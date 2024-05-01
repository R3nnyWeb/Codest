package r3nny.codest.solution.integration.kafka

import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import ru.tinkoff.kora.json.common.annotation.Json
import ru.tinkoff.kora.kafka.common.annotation.KafkaPublisher
import java.util.*

@KafkaPublisher("kafka.producer")
interface KafkaClient {

    @KafkaPublisher.Topic("kafka.producer.send-topic")
    suspend fun send(key: UUID, @Json value: RunCodeRequestEvent)
}