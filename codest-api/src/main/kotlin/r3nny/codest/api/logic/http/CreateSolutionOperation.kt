package r3nny.codest.api.logic.http

import r3nny.codest.api.cache.GetAttemptCache
import r3nny.codest.api.dto.common.TaskInternalDto
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.api.integration.db.TestAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.model.CreateSolutionRequest
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.throwLogicException
import java.util.*

class CreateSolutionOperation(
    private val taskAdapter: TaskAdapter,
    private val attemptsAdapter: AttemptsAdapter,
    private val kafkaAdapter: KafkaAdapter,
    private val getAttemptCache: GetAttemptCache,
    private val testAdapter: TestAdapter
) {

    suspend fun activate(taskId: UUID, userId: UUID, request: CreateSolutionRequest): AttemptDto {
        val language = Language.fromString(request.language)
        val task = getTaskInternal(taskId = taskId, language = language)

        val attempt = attemptsAdapter.saveAttempt(
            taskId = taskId,
            userId = userId,
            code = request.code,
            language = language
        )
        kafkaAdapter.sendCodeToExecute(
            key = attempt.id,
            code = task.driver.replace("\${solution}", request.code),
            language = language,
            input = task.tests.map { it.inputValues }.reduce { acc, it -> acc + it }
        )

        return getAttemptCache.put(attempt.id, attempt)
    }

    private suspend fun getTaskInternal(taskId: UUID, language: Language): TaskInternalDto {
        val task = taskAdapter.getById(taskId) ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND,
            message = "по id = $taskId"
        )
        if (language !in task.languages) throwLogicException(
            code = LogicExceptionCode.LANGUAGE_NOT_ACCEPTABLE,
            message = "по id = $taskId и языку = $language"
        )
        val tests = testAdapter.getAllByTaskId(taskId)
        return task.toInternalDto(language, tests)
    }
}

