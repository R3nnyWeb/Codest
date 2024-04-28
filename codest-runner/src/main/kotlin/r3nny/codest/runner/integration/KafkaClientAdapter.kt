package r3nny.codest.runner.integration

import r3nny.codest.runner.exception.InvocationExceptionCode
import r3nny.codest.runner.helper.wrap
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.logging.common.annotation.Log
import java.util.*

@Component
open class KafkaClientAdapter(
    private val kafkaClient: KafkaClient,
) {

    @Log
    open suspend fun sendCodeRunResponse(id: UUID, response: RunCodeResponseEvent) {
        wrap(
            code = InvocationExceptionCode.KAFKA_EXCEPTION
        ) {
            kafkaClient.send(id, response)
        }
    }

}
