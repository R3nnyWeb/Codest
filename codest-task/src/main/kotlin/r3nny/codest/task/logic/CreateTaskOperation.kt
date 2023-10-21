package r3nny.codest.task.logic

import r3nny.codest.task.integration.mongo.TaskDao

class CreateTaskOperation(
    val taskDao: TaskDao
) {
    suspend fun createTask() {

    }
}