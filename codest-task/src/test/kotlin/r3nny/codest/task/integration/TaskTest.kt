package r3nny.codest.task.integration

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import io.restassured.builder.RequestSpecBuilder
import io.restassured.builder.ResponseSpecBuilder
import io.restassured.config.LogConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.specification.RequestSpecification
import io.restassured.specification.ResponseSpecification
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import r3nny.codest.shared.domain.Language
import r3nny.codest.shared.dto.ErrorDto
import r3nny.codest.task.integration.TaskTest.Specs.requestSpec
import r3nny.codest.task.integration.TaskTest.Specs.responseSpec

class TaskTest : TestBase() {

    object Specs {

        private val logConfig = LogConfig.logConfig().enableLoggingOfRequestAndResponseIfValidationFails(LogDetail.ALL)
        private val config = RestAssuredConfig.config().logConfig(logConfig)

        val requestSpec: RequestSpecification = RequestSpecBuilder()
            .log(LogDetail.ALL)
            .setContentType(ContentType.JSON)
            .setConfig(config)
            .build()

        val responseSpec: ResponseSpecification = ResponseSpecBuilder()
            .log(LogDetail.BODY)
            .build()

    }

    @BeforeEach
    fun beforeEach() {
        taskRepository.deleteAll()
    }

//    @Test
//    fun `success flow - empty tests`(): Unit = runBlocking {
//        Given {
//            spec(requestSpec)
//        }
//        When {
//            get(url())
//        } Then {
//            spec(responseSpec)
//            statusCode(200)
//            body("size()", equalTo(0))
//        }
//    }


    @Test
    fun `success flow`(): Unit = runBlocking {
        Given {
            spec(requestSpec)
        } When {
            body(request)
            post(url())
        } Then {
            spec(responseSpec)
            statusCode(201)
        }

        val savedTask = taskRepository.findAll().last()
        with(savedTask) {
            name shouldBe request.name
            description shouldBe request.description
            enabled shouldBe false
            parameters shouldBe request.parameters
            tests shouldBe request.tests
            drivers.keys shouldBe Language.values().toSet()
        }

    }

    @Test
    fun `error flow build in validation error`(): Unit = runBlocking {
        Given {
            spec(requestSpec)
        } When {
            body("{}")
            post(url())
        } Then {
            statusCode(400)
            val error = extractAs<ErrorDto>()
            error.statusCode shouldBe 400
        }
    }

    @Test
    fun `error flow - validation error`(): Unit = runBlocking {
        val emptyTests = request.copy(tests = emptyList())
        Given {
            spec(requestSpec)
        } When {
            body(emptyTests)
            post(url())
        } Then {
            statusCode(400)
            spec(responseSpec)
            val error = extractAs<ErrorDto>()
            error.statusCode shouldBe 400
            error.errorMessage shouldBe "Количество тестов меньше минимального"//todo: Exceptions codes
        }

    }

}
