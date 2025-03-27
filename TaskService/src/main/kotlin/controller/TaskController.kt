package ru.itmo.controller


import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.itmo.model.TaskDTO
import ru.itmo.service.TaskService

fun Route.taskRoutes(taskService: TaskService) {
    route("api/tasks") {
        post {
            val taskDTO = call.receive<TaskDTO>()
            val createdTask = taskService.createTask(taskDTO)
            call.respond(HttpStatusCode.Created, createdTask)
        }

        get {
            val tasks = taskService.getAllTasks()
            call.respond(tasks)
        }

        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respondText(
                "Invalid task id", status = HttpStatusCode.BadRequest
            )
            val task = taskService.getTaskById(id)
            if (task == null)
                call.respondText("No task with id $id", status = HttpStatusCode.NotFound)
            else
                call.respond(task)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respondText(
                "Invalid task id", status = HttpStatusCode.BadRequest
            )
            val taskDTO = call.receive<TaskDTO>()
            val updatedTask = taskService.updateTask(id, taskDTO)
            if (updatedTask == null)
                call.respondText("No task with id $id", status = HttpStatusCode.NotFound)
            else
                call.respond(updatedTask)
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respondText(
                "Invalid task id", status = HttpStatusCode.BadRequest
            )
            if (taskService.deleteTask(id))
                call.respondText("Task deleted", status = HttpStatusCode.OK)
            else
                call.respondText("No task with id $id", status = HttpStatusCode.NotFound)
        }
    }
}
