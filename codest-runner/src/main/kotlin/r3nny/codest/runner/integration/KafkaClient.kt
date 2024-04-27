package r3nny.codest.runner.integration

import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import ru.tinkoff.kora.json.common.annotation.Json
import ru.tinkoff.kora.kafka.common.annotation.KafkaPublisher
import java.util.UUID

@KafkaPublisher("kafka.producer")
interface KafkaClient {

    @KafkaPublisher.Topic("kafka.producer.send-topic")
    suspend fun send(key: UUID, @Json value: RunCodeResponseEvent)
}