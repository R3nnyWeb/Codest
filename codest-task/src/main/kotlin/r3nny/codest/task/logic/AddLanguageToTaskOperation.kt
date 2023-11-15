package r3nny.codest.task.logic

import org.springframework.stereotype.Service
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.dto.http.AddLanguageToTaskRequest
import r3nny.codest.task.service.driver.DriverGeneratorService
import java.util.*

@Service
class AddLanguageToTaskOperation(
    private val driverGenerator: DriverGeneratorService,
) {

    @LogMethod
    suspend fun activate(taskId: UUID, request: AddLanguageToTaskRequest) {
        throw RuntimeException("Язык есть, епта")

    }

}