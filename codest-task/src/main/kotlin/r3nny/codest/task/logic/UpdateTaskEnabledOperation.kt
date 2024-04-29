package r3nny.codest.task.logic

import java.util.UUID
import ru.tinkoff.kora.logging.common.annotation.Log

open class UpdateTaskEnabledOperation(
//    private val attemptAdapter: AttemptAdapter,
//    private val taskAdapter: TaskAdapter,
) {

    @Log
    open suspend fun activate(taskId: UUID) : Boolean {
//        val solvedLanguages = attemptAdapter.getSolvedLanguagesForTask(taskId)
//        val task = taskAdapter.getById(taskId) ?: throw RuntimeException("задача не найдена");
//        //todo: исключения!!!
//
//        val isAllLanguagesHaveSolution = solvedLanguages.toSet() == task.drivers.keys
//        taskAdapter.updateTaskEnabled(taskId, isAllLanguagesHaveSolution)
//        return isAllLanguagesHaveSolution
       return true
   }
}