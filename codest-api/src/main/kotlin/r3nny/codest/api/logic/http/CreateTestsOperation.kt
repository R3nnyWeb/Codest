package r3nny.codest.api.logic.http

import r3nny.codest.api.exception.LogicExceptionCode
import r3nny.codest.api.integration.db.TaskAdapter
import r3nny.codest.api.integration.db.TestAdapter
import r3nny.codest.api.integration.kafka.KafkaAdapter
import r3nny.codest.model.CreateTest
import r3nny.codest.shared.domain.TestCase
import r3nny.codest.shared.dto.runner.CacheInvalidateEvent
import r3nny.codest.shared.exception.DefaultSecurityExceptionCode
import r3nny.codest.shared.exception.throwLogicException
import r3nny.codest.shared.exception.throwSecurityException
import ru.tinkoff.kora.common.Component
import java.util.*

@Component
class CreateTestsOperation(
    private val testAdapter: TestAdapter,
    private val taskAdapter: TaskAdapter,
    private val kafkaAdapter: KafkaAdapter,
) {

    suspend fun activate(taskId: UUID, userId: UUID, newTests: List<CreateTest>): List<TestCase> {
        taskAdapter.getUserIdByTaskId(taskId)?.let {
            if (it != userId) {
                throwSecurityException(
                    code = DefaultSecurityExceptionCode.FORBIDDEN,
                    message = "Вы не можете редактировать эту задачу"
                )
            }
        } ?: throwLogicException(
            code = LogicExceptionCode.TASK_NOT_FOUND, message = "по id = $taskId"
        )

        val existingTests = testAdapter.getAllByTaskId(taskId).toCreateTest().toSet()

        val insertingTests = newTests.toMutableSet().minus(existingTests)

        if (newTests.any { it.inputData.size != existingTests.first().inputData.size }) {
            throwLogicException(
                code = LogicExceptionCode.TEST_NOT_CORRECT,
                message = "Неверное количество входных данных"
            )
        }

        val map = mutableMapOf<List<String>, String>()
        (existingTests + insertingTests).forEach {
            if (map.containsKey(it.inputData)) {
                if (map[it.inputData] != it.outputData) {
                    throwLogicException(
                        code = LogicExceptionCode.TEST_NOT_CORRECT,
                        message = "Противоречивые тестовые данные"
                    )
                }
            } else map += it.inputData to it.outputData
        }

        val mappedTests = insertingTests.map {
            TestCase(
                id = UUID.randomUUID(),
                inputValues = it.inputData,
                outputValue = it.outputData,
            )
        }

        testAdapter.insert(
            tests = mappedTests,
            taskId = taskId
        )

        kafkaAdapter.sendCacheInvalidate(
            event = CacheInvalidateEvent(
                taskId = taskId
            )
        )
        return mappedTests
    }
}

private fun List<TestCase>.toCreateTest(): List<CreateTest> {
    return this.map {
        CreateTest(
            it.inputValues,
            it.outputValue
        )
    }
}
