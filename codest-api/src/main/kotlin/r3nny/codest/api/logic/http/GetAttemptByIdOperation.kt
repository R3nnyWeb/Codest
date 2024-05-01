package r3nny.codest.api.logic.http

import r3nny.codest.api.dto.dao.AttemptDto
import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.AttemptsAdapter
import r3nny.codest.shared.exception.throwLogicException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class GetAttemptByIdOperation(
    private val attemptsAdapter: AttemptsAdapter
){

    suspend fun activate(id: UUID): AttemptDto = attemptsAdapter.getById(id) ?: throwLogicException(
        code = LogicExceptionCode.ATTEMPT_NOT_FOUND,
        message = "по id =  $id"
    )
}
