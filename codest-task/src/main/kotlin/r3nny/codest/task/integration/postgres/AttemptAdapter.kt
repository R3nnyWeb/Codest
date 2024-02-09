package r3nny.codest.task.integration.postgres

import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.shared.domain.Language
import java.util.*

class AttemptAdapter {

    @LogMethod
    fun getSolvedLanguagesForTask(taskId: UUID): List<Language> {
        return emptyList()
    }
}