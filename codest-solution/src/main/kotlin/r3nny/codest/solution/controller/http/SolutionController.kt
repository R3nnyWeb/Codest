package r3nny.codest.solution.controller.http

import r3nny.codest.solution.api.SolutionApiDelegate
import r3nny.codest.solution.api.SolutionApiResponses
import r3nny.codest.solution.dto.dao.AttemptDto
import r3nny.codest.solution.logic.http.CreateSolutionOperation
import r3nny.codest.solution.model.CreateSolutionRequest
import r3nny.codest.solution.model.SolutionResponse
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class SolutionController(
    private val createSolutionOperation: CreateSolutionOperation
) : SolutionApiDelegate {

    override suspend fun createSolution(
        taskId: UUID,
        xUserId: UUID,
        createSolutionRequest: CreateSolutionRequest,
    ): SolutionApiResponses.CreateSolutionApiResponse {
        return createSolutionOperation.activate(taskId, xUserId, createSolutionRequest).toSolutionApi()
    }

    override suspend fun getSolution(solutionId: UUID, xUserId: UUID): SolutionApiResponses.GetSolutionApiResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSolutionsByTaskId(
        taskId: UUID,
        xUserId: UUID,
    ): SolutionApiResponses.GetSolutionsByTaskIdApiResponse {
        TODO("Not yet implemented")
    }

}

private fun AttemptDto.toSolutionApi() = SolutionApiResponses.CreateSolutionApiResponse.CreateSolution200ApiResponse(
    content = SolutionResponse(
        id = id,
        status = status.api,
        code = code,
        language = language.name,
        createdAt = createdAt,
        _error = error
    )
)
