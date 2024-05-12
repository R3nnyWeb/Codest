package r3nny.codest.api.integration.kafka

import org.slf4j.LoggerFactory
import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.dto.runner.ExecutionTestCase
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import r3nny.codest.shared.exception.wrap
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class KafkaAdapter(
    private val kafkaClient: KafkaClient
) {
    private val logger = LoggerFactory.getLogger(KafkaAdapter::class.java)

    suspend fun sendCacheInvalidate(event: CacheInvalidateEvent) {
        wrap(errorCode = InvocationExceptionCode.KAFKA_ERROR) {
            val key = UUID.randomUUID()
            logger.info("Sending cache invalidate event: $event with key: $key")
            kafkaClient.sendCacheInvalidate(UUID.randomUUID(), event)
        }
    }

    suspend fun sendCodeToExecute(key: UUID, code: String, language: Language, tests: List<TestCase>) {
        wrap(errorCode = InvocationExceptionCode.KAFKA_ERROR) {
            val request = RunCodeRequestEvent(
                code = code,
                tests = tests.map { ExecutionTestCase(
                    inputData = it.inputValues,
                    outputData = it.outputValue
                ) },
                language = language
            )
            kafkaClient.sendRunCodeRequest(key, request)
        }
    }
}