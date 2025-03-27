package com.example.repository

import kotlinx.coroutines.Dispatchers
import model.ProjectDTO
import model.ProjectEntity
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

class ProjectRepository {

    suspend fun createProject(projectDTO: ProjectDTO): ProjectDTO = newSuspendedTransaction(Dispatchers.IO) {
        val projectEntity = ProjectEntity.new {
            name = projectDTO.name
            description = projectDTO.description
            location = projectDTO.location
            startDate = projectDTO.startDate?.let { java.time.LocalDateTime.parse(it) }
            endDate = projectDTO.endDate?.let { java.time.LocalDateTime.parse(it) }
            budget = projectDTO.budget
        }
        projectEntity.toDTO()
    }

    suspend fun getProjectById(id: Long): ProjectDTO? = newSuspendedTransaction(Dispatchers.IO) {
        ProjectEntity.findById(id)?.toDTO()
    }

    suspend fun getAllProjects(): List<ProjectDTO> = newSuspendedTransaction(Dispatchers.IO) {
        ProjectEntity.all().map { it.toDTO() }
    }

    suspend fun updateProject(id: Long, projectDTO: ProjectDTO): ProjectDTO? = newSuspendedTransaction(Dispatchers.IO) {
        val projectEntity = ProjectEntity.findById(id) ?: return@newSuspendedTransaction null

        // Обновляем поля, если они присутствуют в DTO
        projectEntity.name = projectDTO.name
        projectEntity.description = projectDTO.description
        projectEntity.location = projectDTO.location
        projectEntity.startDate = projectDTO.startDate?.let { LocalDateTime.parse(it) }
        projectEntity.endDate = projectDTO.endDate?.let { LocalDateTime.parse(it) }
        projectEntity.budget = projectDTO.budget
        projectEntity.updatedAt = LocalDateTime.now()

        projectEntity.toDTO()
    }

    suspend fun deleteProject(id: Long): Boolean = newSuspendedTransaction(Dispatchers.IO) {
        val projectEntity = ProjectEntity.findById(id)
        if (projectEntity != null) {
            projectEntity.delete()
            true
        } else {
            false
        }
    }
}
