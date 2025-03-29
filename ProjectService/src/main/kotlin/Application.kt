package com.example

import com.example.config.configureDatabases
import com.example.config.configureRouting
import com.example.repository.ProjectRepository
import com.example.service.ProjectService
import com.example.service.impl.ProjectServiceImpl
import configurePrometheus
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Подключение Koin и регистрация зависимостей
    install(Koin) {
        modules(
            org.koin.dsl.module {
                single { ProjectRepository() }
                single<ProjectService> { ProjectServiceImpl(get()) }
            },
        )
    }

    install(ContentNegotiation) {
        json()
    }

    configureDatabases()
    configureRouting()
    configurePrometheus()

    println("SERVER STARTED ON PORT ${environment.config.property("ktor.deployment.port").getString()}")

}
