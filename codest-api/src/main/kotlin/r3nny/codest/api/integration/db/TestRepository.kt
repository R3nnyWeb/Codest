package r3nny.codest.api.integration.db

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.TestCase
import ru.tinkoff.kora.database.common.annotation.Batch
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository
import java.util.*

@Repository
interface TestRepository: JdbcRepository {

    @Query("INSERT INTO tests(id, input_values, output_value, task_id) VALUES (:test.id, :test.inputValues, :test.outputValue, :taskId)")
    @LogMethod
    fun insert(@Batch test: List<TestCase>, taskId: UUID)

    @Query("SELECT id, input_values, output_value FROM tests WHERE task_id = :taskId")
    @LogMethod
    fun getAllByTaskId(taskId: UUID): List<TestCase>

}
