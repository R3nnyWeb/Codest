package r3nny.codest.task.controller.http

import kotlinx.coroutines.runBlocking
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.dao.TaskDTO
import r3nny.codest.task.dto.http.CreateTaskRequest
import r3nny.codest.task.integration.mongo.TaskRepository
import r3nny.codest.task.logic.CreateTaskOperation
import java.util.UUID

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val createTaskOperation: CreateTaskOperation,
    private val taskRepository: TaskRepository //Todo: just for test
) {

    @GetMapping()
    fun getAll(): ResponseEntity<List<TaskDTO>> = runBlocking {
        ResponseEntity(taskRepository.findAll(), HttpStatus.OK)
    }

    @PostMapping()
    fun createTask(@RequestBody request: CreateTaskRequest): ResponseEntity<UUID> = runBlocking {
       ResponseEntity(createTaskOperation.activate(request), HttpStatus.CREATED)
    }

}
