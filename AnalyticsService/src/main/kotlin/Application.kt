package ru.itmo

import config.configurePrometheus
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import ru.itmo.config.*
import ru.itmo.repository.TaskEventRepository
import ru.itmo.service.TaskEventService
import ru.itmo.service.impl.TaskEventServiceImpl

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    kafkaConfig()


    install(Koin) {
        modules(
            appModule
        )
    }

    configureSerialization()
    configureDatabases()
    configureRouting()
    configurePrometheus()
    configureKafka()

    println("SERVER STARTED ON PORT ${environment.config.property("ktor.deployment.port").getString()}")

}
