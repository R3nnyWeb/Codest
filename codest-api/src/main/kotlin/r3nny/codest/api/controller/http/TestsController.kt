package r3nny.codest.api.controller.http

import r3nny.codest.api.TestsApiDelegate
import r3nny.codest.api.TestsApiResponses
import r3nny.codest.api.logic.http.CreateTestsOperation
import r3nny.codest.api.logic.http.DeleteTestOperation
import r3nny.codest.model.CreateTest
import r3nny.codest.model.Test
import r3nny.codest.shared.PrincipalImpl
import r3nny.codest.shared.domain.TestCase
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.common.Principal
import java.util.*

@Component
class TestsController(
    private val deleteTestOperation: DeleteTestOperation,
    private val createTestsOperation: CreateTestsOperation
) : TestsApiDelegate {
    override suspend fun createTests(
        taskId: UUID,
        createTest: List<CreateTest>,
    ): TestsApiResponses.CreateTestsApiResponse {
        return createTestsOperation.activate(taskId, (Principal.current() as PrincipalImpl).userId, createTest).toResponse()
    }

    override suspend fun deleteTest(testId: UUID): TestsApiResponses.DeleteTestApiResponse {
        deleteTestOperation.activate(testId = testId, userId = (Principal.current() as PrincipalImpl).userId)
        return TestsApiResponses.DeleteTestApiResponse.DeleteTest200ApiResponse()
    }
}

private fun List<TestCase>.toResponse()= TestsApiResponses.CreateTestsApiResponse.CreateTests200ApiResponse(
    content = this.map {
        Test(
            it.id,
            it.inputValues,
            it.outputValue
        )
    }
)
