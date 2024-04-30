package r3nny.codest.solution.logic.http

import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.exception.throwInvocationException
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.solution.dto.dao.AttemptDto
import r3nny.codest.solution.exception.InvocationExceptionCode
import r3nny.codest.solution.exception.LogicExceptionCode
import r3nny.codest.solution.integration.dao.AttemptsAdapter
import r3nny.codest.solution.integration.http.TaskAdapter
import r3nny.codest.solution.integration.http.TaskAdapter.Companion.LANGUAGE_NOT_ACCEPTABLE_ERROR
import r3nny.codest.solution.integration.http.TaskAdapter.Companion.TASK_NOT_FOUND_ERROR
import r3nny.codest.solution.integration.http.task.model.InternalTaskResponse
import r3nny.codest.solution.integration.kafka.KafkaAdapter
import r3nny.codest.solution.model.CreateSolutionRequest
import java.util.*

class CreateSolutionOperation(
    private val taskAdapter: TaskAdapter,
    private val attemptsAdapter: AttemptsAdapter,
    private val kafkaAdapter: KafkaAdapter,
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
            code = task.driver.replace("\${solution}", request.code),
            language = language,
            input = task.tests.map { it.inputData }.reduce { acc, it -> acc + it }
        )
        return attempt
    }

    private suspend fun getTaskInternal(taskId: UUID, language: Language): InternalTaskResponse {
        val response = taskAdapter.getTaskInternal(taskId = taskId, language = language)
        if (response.isRight) {
            val code = when (response.right()) {
                TASK_NOT_FOUND_ERROR -> LogicExceptionCode.TASK_NOT_FOUND
                LANGUAGE_NOT_ACCEPTABLE_ERROR -> LogicExceptionCode.LANGUAGE_NOT_ACCEPTABLE
                else -> throwInvocationException(
                    code = InvocationExceptionCode.TASK_API, message = "Код: ${response.right()}."
                )
            }
            throwLogicException(
                code = code, message = "для taskId = $taskId"
            )
        } else {
            return response.left()
        }
    }
}