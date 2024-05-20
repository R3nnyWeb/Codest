package r3nny.codest.api.integration.db

import r3nny.codest.api.dto.common.Level
import r3nny.codest.api.dto.common.TaskLiteDto
import r3nny.codest.api.dto.dao.TaskDto
import r3nny.codest.api.dto.http.TaskListFrontend
import r3nny.codest.logging.aspect.LogMethod
import ru.tinkoff.kora.database.common.annotation.Query
import ru.tinkoff.kora.database.common.annotation.Repository
import ru.tinkoff.kora.database.jdbc.JdbcRepository
import java.util.*

@Repository
interface TaskRepository : JdbcRepository {

    @Query(
        """
        INSERT INTO tasks (id, name, method_name, drivers, start_code, languages, is_enabled, is_private, description, level, input_types, output_type, user_id)
        VALUES (:task.id, :task.name, :task.methodName, :task.drivers::jsonb,
        :task.startCode::jsonb, :task.languages, :task.isEnabled, :task.isPrivate, :task.description,
        :task.level, :task.inputTypes, :task.outputType, :task.userId)
    """
    )
    @LogMethod
    fun insert(task: TaskDto)

    @Query(
        """
        SELECT id, name, level, languages, is_enabled, is_private, description, start_code, level
        FROM tasks
        WHERE id = :taskId
    """
    )
    suspend fun getLiteById(taskId: UUID): TaskLiteDto?

    @Query(
        """
        SELECT id, name, method_name, drivers, start_code, languages, is_enabled, is_private, description, level, input_types, output_type, user_id
        FROM tasks
        WHERE id = :taskId
    """
    )
    suspend fun getFullById(taskId: UUID): TaskDto?

    @Query(
        """
        SELECT id, name, level
        FROM tasks
        WHERE (
            (:level is null or level = :level) and (:query is null or name like '%' || :query || '%')
        )
        ORDER BY name
        LIMIT :limit
        OFFSET :offset
        """
    )
    suspend fun getList(query: String?, level: Level?, offset: Long, limit: Int): List<TaskListFrontend>

    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun count(): Long


    @Query("SELECT user_id FROM tasks WHERE id = :taskId")
    suspend fun getUserIdByTaskId(taskId: UUID): UUID?

    @Query("SELECT id, name, method_name, drivers, start_code, languages, is_enabled, is_private, description, level, input_types, output_type, user_id FROM tasks WHERE user_id = :userId")
    suspend fun getAllByUserId(userId: UUID): List<TaskDto>

    @Query("UPDATE tasks SET is_enabled = :enable WHERE id = :taskId")
    suspend fun updateEnable(taskId: UUID, enable: Boolean)
}