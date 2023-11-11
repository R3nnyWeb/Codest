package r3nny.codest.task.logic

import r3nny.codest.shared.domain.Language
import r3nny.codest.task.integration.mongo.TaskAdapter
import r3nny.codest.task.integration.postgres.AttemptAdapter
import java.util.UUID

class UpdateTaskEnabledOperation(
    private val attemptAdapter: AttemptAdapter,
    private val taskAdapter: TaskAdapter,
) {

    suspend fun activate(taskId: UUID) : Boolean {
        val solvedLanguages = attemptAdapter.getSolvedLanguagesForTask(taskId)
        //todo: исключения!!!

        val isAllLanguagesHaveSolution = solvedLanguages.sorted() == Language.values().sorted()
        taskAdapter.updateTaskEnabled(taskId, isAllLanguagesHaveSolution)
        return isAllLanguagesHaveSolution
    }
}