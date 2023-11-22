package r3nny.codest.task.integration.mongo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import r3nny.codest.task.dto.dao.Level
import r3nny.codest.task.dto.dao.TaskDTO
import java.util.UUID

@Repository
interface TaskRepository : MongoRepository<TaskDTO, UUID > {
    fun findByLevelAndEnabledAndNameContaining(level: Level, enabled: Boolean, name: String, pageable: Pageable) : Page<TaskDTO>
}