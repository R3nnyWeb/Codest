package r3nny.codest.task

import com.sksamuel.hoplite.ConfigLoaderBuilder
import com.sksamuel.hoplite.addResourceSource
import r3nny.codest.task.config.AppConfig

fun main(){
    val config = ConfigLoaderBuilder.default()
        .addResourceSource("/config.json")
        .build()
        .loadConfigOrThrow<AppConfig>()
    println(config);
}