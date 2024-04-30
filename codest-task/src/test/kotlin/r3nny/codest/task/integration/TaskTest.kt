package r3nny.codest.task.integration


class TaskTest  {

   /* object Specs {

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
     //   taskRepository.deleteAll()
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
    fun `success flow get taskList disabled`(): Unit = runBlocking {
        val id = createTaskOperation.activate(request);

        Given {
            spec(requestSpec)
        } When {
            get(url())
        } Then {
            spec(responseSpec)
            statusCode(200)
            val result = extractAs<Page<TaskListFrontend>>()
            result.size shouldBe  0
        }
    }

    @Test
    fun `success flow create task`(): Unit = runBlocking {
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
            methodName shouldBe request.methodName
            parameters shouldBe request.parameters
            tests shouldBe request.tests
            drivers.keys shouldBe Language.values().toSet()
        }

    }

    @Test
    fun `success flow get task internal`(): Unit = runBlocking {
        val id = createTaskOperation.activate(request)
        Given {
            spec(requestSpec)
        } When {
            queryParam("language", Language.JAVA)
            get("${url()}/$id")
        } Then {
            spec(responseSpec)
            statusCode(200)
            val result = extractAs<TaskInternalDTO>()
            val savedTask = taskRepository.findAll().last()
            with(result) {
                taskId shouldBe id
                tests shouldBe savedTask.tests
                driver shouldBe savedTask.drivers[Language.JAVA]
            }
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
*/
}
