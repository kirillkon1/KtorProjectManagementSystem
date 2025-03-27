package com.example.service.impl

import com.example.repository.ProjectRepository
import com.example.service.ProjectService
import model.ProjectDTO

class ProjectServiceImpl(private val projectRepository: ProjectRepository): ProjectService {
    override suspend fun createProject(projectDTO: ProjectDTO): ProjectDTO {
        return projectRepository.createProject(projectDTO)
    }

    override suspend fun getProjectById(id: Long): ProjectDTO? {
        return projectRepository.getProjectById(id)
    }

    override suspend fun getAllProjects(): List<ProjectDTO> {
        return projectRepository.getAllProjects()
    }

    override suspend fun updateProject(id: Long, projectDTO: ProjectDTO): ProjectDTO? {
        return projectRepository.updateProject(id, projectDTO)
    }

    override suspend fun deleteProject(id: Long): Boolean {
        return projectRepository.deleteProject(id)
    }
}