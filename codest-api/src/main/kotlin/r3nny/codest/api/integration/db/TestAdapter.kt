package r3nny.codest.api.integration.db

import r3nny.codest.api.cache.TestsByTaskIdCache
import r3nny.codest.api.exception.InvocationExceptionCode
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.exception.wrap
import ru.tinkoff.kora.cache.annotation.Cacheable
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
open class TestAdapter(
    private val testRepository: TestRepository,
) {

    @LogMethod
    @Cacheable(TestsByTaskIdCache::class)
    open suspend fun getAllByTaskId(taskId: UUID): List<TestCase> {
        return wrap(errorCode = InvocationExceptionCode.TEST_ERROR) {
            testRepository.getAllByTaskId(taskId)
        }
    }

    suspend fun delete(testId: UUID) {
        return wrap(errorCode = InvocationExceptionCode.TEST_ERROR) {
            testRepository.delete(testId)
        }
    }

    suspend fun getTaskIdByTestId(testId: UUID): UUID? {
        return wrap(errorCode = InvocationExceptionCode.TEST_ERROR) {
            testRepository.getTaskIdByTestId(testId)
        }
    }

    suspend fun insert(tests: List<TestCase>, taskId: UUID) {
        return wrap(errorCode = InvocationExceptionCode.TEST_ERROR) {
            testRepository.insert(tests, taskId)
        }
    }

}
