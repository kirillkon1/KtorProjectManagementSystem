package ru.itmo.controller

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itmo.service.TaskEventService

fun Route.taskEventRoutes(taskEventService: TaskEventService) {
    route("api/analytics/tasks") {

        get {
            val tasks = taskEventService.getAllTaskEvents()
            call.respond(tasks)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respondText(
                "Invalid taskEvent id", status = HttpStatusCode.BadRequest
            )
            val task = taskEventService.getTaskEventById(id)
            if (task == null)
                call.respondText("No taskEvent with id $id", status = HttpStatusCode.NotFound)
            else
                call.respond(task)
        }

        post("{id}"){
            val id = call.parameters["id"]?.toLongOrNull() ?: return@post call.respondText(
                "Invalid taskEvent id", status = HttpStatusCode.BadRequest
            )

            taskEventService.createRequest(id)
            call.respondText("Request sent", status = HttpStatusCode.Created)
        }

    }
}
