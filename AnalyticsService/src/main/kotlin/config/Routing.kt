package ru.itmo.config

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.itmo.controller.taskEventRoutes
import ru.itmo.service.TaskEventService

fun Application.configureRouting() {

    val taskEventService: TaskEventService by inject<TaskEventService>()
    routing {
        taskEventRoutes(taskEventService)
    }
}
