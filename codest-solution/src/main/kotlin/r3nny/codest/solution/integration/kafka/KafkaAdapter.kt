package r3nny.codest.solution.integration.kafka

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.exception.wrap
import r3nny.codest.solution.exception.InvocationExceptionCode
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class KafkaAdapter(
    private val kafkaClient: KafkaClient
) {


    suspend fun sendCodeToExecute(key: UUID, code: String, language: Language, input: List<String>) {
        wrap(errorCode = InvocationExceptionCode.KAFKA) {
            val request = RunCodeRequestEvent(
                code = code,
                input = input,
                language = language
            )
            kafkaClient.send(key, request)
        }
    }
}
