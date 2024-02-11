package r3nny.codest.runner.integration

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.dto.runner.RunCodeResponseEvent
import java.util.UUID

class KafkaClientAdapter {

    @LogMethod
    suspend fun sendCodeRunResponse(id: UUID, response: RunCodeResponseEvent) {

    }

}
