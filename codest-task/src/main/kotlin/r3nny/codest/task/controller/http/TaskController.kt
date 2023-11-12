package r3nny.codest.task.controller.http

import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskRepository
import r3nny.codest.task.logic.CreateTaskOperation
import r3nny.codest.task.logic.GetTaskInternalOperation
import java.util.UUID

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val getTaskInternalOperation: GetTaskInternalOperation,
    private val repository: TaskRepository,
) {

    @GetMapping
    fun getAll(): List<TaskDTO> {
        return repository.findAll()
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
