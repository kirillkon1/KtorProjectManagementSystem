package controller

import com.example.service.ProjectService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import model.ProjectDTO

fun Route.projectRoutes(projectService: ProjectService) {
    route("/api/projects") {
        // Создание проекта
        post {
            val projectDTO = call.receive<ProjectDTO>()
            val createdProject = projectService.createProject(projectDTO)
            call.respond(HttpStatusCode.Created, createdProject)
        }

        // Получение списка проектов
        get {
            val projects = projectService.getAllProjects()
            call.respond(projects)
        }

        // Получение проекта по id
        get("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respondText(
                "Invalid project id", status = HttpStatusCode.BadRequest
            )
            val project = projectService.getProjectById(id)
            if (project == null)
                call.respondText("No project with id $id", status = HttpStatusCode.NotFound)
            else
                call.respond(project)
        }

        // Обновление проекта по id
        put("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@put call.respondText(
                "Invalid project id", status = HttpStatusCode.BadRequest
            )
            val projectDTO = call.receive<ProjectDTO>()
            val updatedProject = projectService.updateProject(id, projectDTO)
            if (updatedProject == null)
                call.respondText("No project with id $id", status = HttpStatusCode.NotFound)
            else
                call.respond(updatedProject)
        }

        // Удаление проекта по id
        delete("{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@delete call.respondText(
                "Invalid project id", status = HttpStatusCode.BadRequest
            )
            if (projectService.deleteProject(id))
                call.respondText("Project deleted", status = HttpStatusCode.OK)
            else
                call.respondText("No project with id $id", status = HttpStatusCode.NotFound)
        }
    }
}
