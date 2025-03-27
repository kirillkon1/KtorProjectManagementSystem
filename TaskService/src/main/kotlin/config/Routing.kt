package ru.itmo.modules

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.itmo.controller.taskRoutes
import ru.itmo.service.TaskService

fun Application.configureRouting() {

    val taskService: TaskService by inject<TaskService>()

    routing {
        taskRoutes(taskService)
    }

}
