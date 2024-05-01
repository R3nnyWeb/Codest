package r3nny.codest.api.integration.kafka

import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.exception.wrap
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class KafkaAdapter(
    private val kafkaClient: KafkaClient
) {

    suspend fun sendCacheInvalidate(event: CacheInvalidateEvent) {
        wrap(errorCode = InvocationExceptionCode.KAFKA_ERROR) {
            kafkaClient.sendCacheInvalidate(UUID.randomUUID(), event)
        }
    }

    suspend fun sendCodeToExecute(key: UUID, code: String, language: Language, input: List<String>) {
        wrap(errorCode = InvocationExceptionCode.KAFKA_ERROR) {
            val request = RunCodeRequestEvent(
                code = code,
                input = input,
                language = language
            )
            kafkaClient.sendRunCodeRequest(key, request)
        }
    }
}