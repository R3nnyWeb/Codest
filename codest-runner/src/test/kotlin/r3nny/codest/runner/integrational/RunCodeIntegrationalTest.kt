package r3nny.codest.runner.integrational

import io.goodforgod.testcontainers.extensions.ContainerMode
import io.goodforgod.testcontainers.extensions.kafka.*
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.testcontainers.shaded.org.awaitility.Awaitility
import r3nny.codest.runner.App
import r3nny.codest.runner.controller.KafkaConsumer
import r3nny.codest.runner.integration.KafkaClient
import r3nny.codest.shared.dto.runner.ExecutableLanguage
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest
import ru.tinkoff.kora.test.extension.junit5.KoraAppTestConfigModifier
import ru.tinkoff.kora.test.extension.junit5.KoraConfigModification
import ru.tinkoff.kora.test.extension.junit5.TestComponent
import java.time.Duration
import java.util.concurrent.Executors

@TestcontainersKafka(mode = ContainerMode.PER_RUN, topics = Topics("codest.runner.request"))
@KoraAppTest(App::class)
class RunCodeIntegrationalTest : KoraAppTestConfigModifier {

    @ContainerKafkaConnection
    private lateinit var kafkaConnection: KafkaConnection

    @TestComponent
    private lateinit var kafkaConsumer: KafkaConsumer

    @TestComponent
    private lateinit var kafkaClient: KafkaClient
    override fun config(): KoraConfigModification {
        return KoraConfigModification
            .ofSystemProperty("KAFKA_BOOTSTRAP", kafkaConnection.params().bootstrapServers())
    }

    @Test
    fun success() {
        val topic = "codest.runner.request"
        val event = RunCodeRequestEvent(
            code = "class Solution { public static void main(String[] args) { System.out.println(\"Hello World!\"); } }",
            input = null,
            language = ExecutableLanguage.JAVA_17,
        )
        val json = JSONObject().put("code", event.code).put("input", event.input).put("language", event.language)

        kafkaConnection.send(topic, Event.ofValueAndRandomKey(json))
        val consumer = kafkaConnection.subscribe("codest.runner.response")

        Awaitility.await()
            .atMost(Duration.ofSeconds(60))
            .pollExecutorService(Executors.newSingleThreadExecutor())
            .until { consumer.received.isPresent }

    }


}