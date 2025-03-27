@file:Suppress("ktlint:standard:no-wildcard-imports")

package ru.itmo.config

import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.itmo.controller.userRoutes
import ru.itmo.service.AuthService
import ru.itmo.service.UserService
import controller.authRoutes

fun Application.configureHTTP() {
    // Инжектим UserService на уровне Application
    val userService: UserService by inject()

    val authService: AuthService by inject()

    routing {

//        authenticate("auth-jwt") {
//            userRoutes(userService)
//        }
        userRoutes(userService)
        swaggerUI(path = "openapi")
        authRoutes(authService)
    }
}
