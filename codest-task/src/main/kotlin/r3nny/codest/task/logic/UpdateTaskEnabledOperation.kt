package r3nny.codest.task.logic

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.integration.postgres.AttemptAdapter
import java.lang.RuntimeException
import java.util.UUID

class UpdateTaskEnabledOperation(
    private val attemptAdapter: AttemptAdapter,
    private val taskAdapter: TaskAdapter,
) {

    @LogMethod
    suspend fun activate(taskId: UUID) : Boolean {
        val solvedLanguages = attemptAdapter.getSolvedLanguagesForTask(taskId)
        val task = taskAdapter.getById(taskId) ?: throw RuntimeException("задача не найдена");
        //todo: исключения!!!

        val isAllLanguagesHaveSolution = solvedLanguages.toSet() == task.drivers.keys
        taskAdapter.updateTaskEnabled(taskId, isAllLanguagesHaveSolution)
        return isAllLanguagesHaveSolution
    }
}