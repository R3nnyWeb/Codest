package r3nny.codest.api.controller.http

import r3nny.codest.api.SolutionApiDelegate
import r3nny.codest.api.SolutionApiResponses
import r3nny.codest.api.dto.dao.AttemptByTaskDto.Companion.toResponse
import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.logic.http.CreateSolutionOperation
import r3nny.codest.api.logic.http.GetAttemptByIdOperation
import r3nny.codest.api.logic.http.GetUserSolutionsForTaskOperation
import r3nny.codest.model.CreateSolutionRequest
import r3nny.codest.model.SolutionLiteResponse
import r3nny.codest.model.SolutionResponse
import r3nny.codest.shared.PrincipalImpl
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Principal
import java.util.*

@Component
class SolutionController(
    private val createSolutionOperation: CreateSolutionOperation,
    private val getAttemptByIdOperation: GetAttemptByIdOperation,
    private val getUserSolutionsForTaskOperation: GetUserSolutionsForTaskOperation,
) : SolutionApiDelegate {

    override suspend fun createSolution(
        taskId: UUID,
        createSolutionRequest: CreateSolutionRequest,
    ): SolutionApiResponses.CreateSolutionApiResponse {
        val dto = createSolutionOperation.activate(
            taskId,
            (Principal.current() as PrincipalImpl).userId,
            createSolutionRequest
        )
        return SolutionApiResponses.CreateSolutionApiResponse.CreateSolution200ApiResponse(dto.toSolutionApi())
    }

    override suspend fun getSolution(solutionId: UUID): SolutionApiResponses.GetSolutionApiResponse {
        val dto = getAttemptByIdOperation.activate(solutionId).toSolutionApi()
        return SolutionApiResponses.GetSolutionApiResponse.GetSolution200ApiResponse(dto)
    }

    override suspend fun getTaskSolutions(taskId: UUID): SolutionApiResponses.GetTaskSolutionsApiResponse {
        val response : List<SolutionLiteResponse> = getUserSolutionsForTaskOperation.activate((Principal.current() as PrincipalImpl).userId, taskId).toResponse()
        return SolutionApiResponses.GetTaskSolutionsApiResponse.GetTaskSolutions200ApiResponse(response)
    }

}


private fun AttemptDto.toSolutionApi() =
    SolutionResponse(
        id = id,
        status = status.api,
        code = code,
        language = language.name.lowercase(),
        createdAt = createdAt,
        _error = error
    )

