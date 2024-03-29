package r3nny.codest.task

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import r3nny.codest.logging.aspect.LogMethod
import r3nny.codest.task.config.AppConfig

@SpringBootApplication
@Configuration
@EnableMongoRepositories
class TaskApplication {

    @Bean
    fun getConfig(): AppConfig {
        return ConfigLoaderBuilder.default()
            .addResourceSource("/config.json")
            .allowUnresolvedSubstitutions()
            .build()
            .loadConfigOrThrow<AppConfig>()
    }
}


fun main(args: Array<String>) {

    runApplication<TaskApplication>(*args)

}
