package r3nny.codest.solution.integration.kafka

import r3nny.codest.shared.domain.Language

class KafkaAdapter {


    suspend fun sendCodeToExecute(code: String, language: Language, input: List<String>) {}
}
