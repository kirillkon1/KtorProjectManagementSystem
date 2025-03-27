package com.example.config

import com.example.service.ProjectService
import controller.projectRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {

    val projectService: ProjectService by inject<ProjectService>()

    routing {
        projectRoutes(projectService)
    }
}
