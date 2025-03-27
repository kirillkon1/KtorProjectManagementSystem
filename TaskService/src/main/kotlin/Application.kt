package ru.itmo

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.Koin
import config.configureDatabases
import configurePrometheus
import ru.itmo.config.kafkaConfigure
import ru.itmo.modules.configureRouting
import ru.itmo.repository.TaskRepository
import ru.itmo.service.TaskService
import ru.itmo.service.impl.TaskServiceImpl

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
        modules(
            org.koin.dsl.module {
                single { TaskRepository() }
                single<TaskService> { TaskServiceImpl(get()) }
            },
        )
    }

    install(ContentNegotiation) {
        json()
    }

    configureDatabases()
    configureRouting()
    configurePrometheus()
    kafkaConfigure()
}
