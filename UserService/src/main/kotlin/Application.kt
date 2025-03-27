@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo

import ru.itmo.config.configurePrometheus
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import repository.UserRepository
import ru.itmo.config.*
import ru.itmo.service.AuthService
import ru.itmo.service.UserService
import ru.itmo.service.impl.AuthServiceImpl
import ru.itmo.service.impl.UserServiceImpl

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    // Подключение Koin и регистрация зависимостей
    install(Koin) {
        modules(
            org.koin.dsl.module {
                single { UserRepository() }
                single<UserService> { UserServiceImpl(get()) }
                single<AuthService> { AuthServiceImpl(get(), jwtConfig()) }
            },
        )
    }

    configurePrometheus()
    configureSecurity()
    configureSerialization()
    configureHTTP()
    configureRouting()
    configureDatabases()
}
