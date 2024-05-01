package r3nny.codest.api.integration.db

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.exception.wrap
import r3nny.codest.api.cache.TestsByTaskIdCache
import r3nny.codest.api.exception.InvocationExceptionCode
import ru.tinkoff.kora.cache.annotation.Cacheable
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class TestAdapter(
    private val testRepository: TestRepository
) {

    @LogMethod
    @Cacheable(TestsByTaskIdCache::class)
    open suspend fun getAllByTaskId(taskId: UUID): List<TestCase> {
       return wrap(errorCode = InvocationExceptionCode.GET_TEST_ERROR) {
           testRepository.getAllByTaskId(taskId)
        }
    }
}