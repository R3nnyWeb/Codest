package r3nny.codest.runner.service.executors

import r3nny.codest.runner.service.ExecutionResult
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.runner.RunCodeRequestEvent

interface CodeExecutor {
    val languages: Set<Language>
   suspend fun execute(code: String, language: Language, input: List<String>?): Pair<ExecutionResult, ExecutionResult>
   suspend fun execute(request: RunCodeRequestEvent): Pair<ExecutionResult, ExecutionResult>
}