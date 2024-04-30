package r3nny.codest.task.integration.db

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.exception.wrap
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.exception.InvocationExceptionCode
import ru.tinkoff.kora.common.Component
import ru.tinkoff.kora.database.jdbc.JdbcHelper

@Component
open class TaskAndTestAdapter(
    private val taskRepository: TaskRepository,
    private val testRepository: TestRepository,
) {
    val tx = taskRepository.jdbcConnectionFactory
    fun updateLanguage(updatedTask: TaskDto) {

    }

    @LogMethod
    suspend fun createTask(task: TaskDto, tests: List<TestCase>) {
        wrap(errorCode = InvocationExceptionCode.CREATE_TASK_ERROR) {
            tx.inTx(JdbcHelper.SqlRunnable {
                taskRepository.insert(task)
                testRepository.insert(tests, task.id)
            })
        }
    }

    fun update(capture: TaskDto) {

    }

}
