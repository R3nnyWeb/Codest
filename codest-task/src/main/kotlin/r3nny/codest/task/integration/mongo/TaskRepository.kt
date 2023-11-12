package r3nny.codest.task.integration.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import r3nny.codest.task.dto.dao.TaskDTO
import java.util.UUID

@Repository
interface TaskRepository : MongoRepository<TaskDTO, UUID >