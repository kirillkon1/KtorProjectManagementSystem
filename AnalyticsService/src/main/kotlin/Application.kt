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
}
