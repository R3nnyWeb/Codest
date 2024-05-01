package r3nny.codest.api.controller.http

import r3nny.codest.api.SolutionApiDelegate
import r3nny.codest.api.SolutionApiResponses
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.logic.http.CreateSolutionOperation
import r3nny.codest.api.logic.http.GetAttemptByIdOperation
import r3nny.codest.model.CreateSolutionRequest
import r3nny.codest.model.SolutionResponse
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class SolutionController(
    private val createSolutionOperation: CreateSolutionOperation,
    private val getAttemptByIdOperation: GetAttemptByIdOperation,
) : SolutionApiDelegate {

    override suspend fun createSolution(
        taskId: UUID,
        xUserId: UUID,
        createSolutionRequest: CreateSolutionRequest,
    ): SolutionApiResponses.CreateSolutionApiResponse {
        val dto = createSolutionOperation.activate(taskId, xUserId, createSolutionRequest)
        return SolutionApiResponses.CreateSolutionApiResponse.CreateSolution200ApiResponse(dto.toSolutionApi())
    }

    override suspend fun getSolution(solutionId: UUID): SolutionApiResponses.GetSolutionApiResponse {
        val dto = getAttemptByIdOperation.activate(solutionId).toSolutionApi()
        return SolutionApiResponses.GetSolutionApiResponse.GetSolution200ApiResponse(dto)
    }

}

private fun AttemptDto.toSolutionApi() =
    SolutionResponse(
        id = id,
        status = status.api,
        code = code,
        language = language.name,
        createdAt = createdAt,
        _error = error
    )

