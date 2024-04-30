package r3nny.codest.solution.controller.http

import r3nny.codest.solution.api.SolutionApiDelegate
import r3nny.codest.solution.api.SolutionApiResponses
import r3nny.codest.solution.model.CreateSolutionRequest
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class SolutionController : SolutionApiDelegate {

    override suspend fun createSolution(
        taskId: UUID,
        xUserId: UUID,
        createSolutionRequest: CreateSolutionRequest,
    ): SolutionApiResponses.CreateSolutionApiResponse {
        TODO("Not yet implemented")
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