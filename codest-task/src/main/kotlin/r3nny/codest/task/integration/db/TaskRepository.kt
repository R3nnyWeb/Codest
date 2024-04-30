package r3nny.codest.task.integration.db

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.dao.TaskDto
import r3nny.codest.task.dto.dao.TaskLiteDto
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository
import java.util.*

@Repository
interface TaskRepository: JdbcRepository {

    @Query("""
        INSERT INTO tasks (id, name, method_name, drivers, start_code, languages, is_enabled, is_private, description, level, input_types, output_type)
        VALUES (:task.id, :task.name, :task.methodName, :task.drivers::jsonb,
        :task.startCode::jsonb, :task.languages, :task.isEnabled, :task.isPrivate, :task.description,
        :task.level, :task.inputTypes, :task.outputType)
    """)
    @LogMethod
    fun insert(task: TaskDto)

    @Query("""
        SELECT id, name, level, languages, is_enabled, is_private, description, start_code, level
        FROM tasks
        WHERE id = :taskId
    """)
    suspend fun getLiteById(taskId: UUID): TaskLiteDto?

    @Query("""
        SELECT id, name, method_name, drivers, start_code, languages, is_enabled, is_private, description, level, input_types, output_type
        FROM tasks
        WHERE id = :taskId
    """)
    suspend fun getFullById(taskId: UUID): TaskDto?

}