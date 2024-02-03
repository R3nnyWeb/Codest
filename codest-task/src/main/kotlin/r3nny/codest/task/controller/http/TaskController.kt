package r3nny.codest.task.controller.http

import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Page
import org.springframework.data.mongodb.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.dao.Level
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.dto.http.TaskListFrontend
import r3nny.codest.task.integration.mongo.TaskRepository
import r3nny.codest.task.integration.mongo.criteria.TaskSearchQuery
import r3nny.codest.task.logic.CreateTaskOperation
import r3nny.codest.task.logic.GetTasksListFrontendOperation
import r3nny.codest.task.logic.GetTaskInternalOperation
import java.util.UUID

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val getTaskInternalOperation: GetTaskInternalOperation,
    private val getTasksListFrontendOperation: GetTasksListFrontendOperation,
) {

//    @GetMapping
//     fun getAll(): List<TaskDTO> {
//        return repository.findAll()
//    }

    @GetMapping
    fun getFrontendList(
        @RequestParam(required = false) search: String,
        @RequestParam(required = false) level: Level,
        @RequestParam(required = false) enabled: Boolean = false,
        @RequestParam(required = false, defaultValue = "10") limit: Int,
        @RequestParam(required = false, defaultValue = "0") offset: Int,
    ): Page<TaskListFrontend> = runBlocking { //todo: webflux
        getTasksListFrontendOperation.activate(
            query = TaskSearchQuery(
                name = search,
                level = level,
                enabled = enabled
            ),
            limit = limit,
            offset = offset
        )
    }

    @GetMapping("/{id}")
    fun getInternal(@PathVariable id: UUID, @RequestParam language: Language): ResponseEntity<Any> = runBlocking {
        ResponseEntity.ok(getTaskInternalOperation.activate(id, language))
    }

    //todo: Реактивность
    @PostMapping()
    fun createTask(@RequestBody request: CreateTaskRequest): ResponseEntity<UUID> = runBlocking {
        ResponseEntity(createTaskOperation.activate(request), HttpStatus.CREATED)
    }

}
