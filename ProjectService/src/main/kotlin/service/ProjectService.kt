package com.example.service

import model.ProjectDTO

/**
 * Интерфейс для работы с проектами.
 * Предоставляет методы для создания, получения, обновления и удаления проектов.
 */
interface ProjectService {

    /**
     * Создает новый проект на основе переданных данных.
     *
     * @param projectDTO DTO с информацией о проекте для создания.
     * @return DTO созданного проекта.
     */
    suspend fun createProject(projectDTO: ProjectDTO): ProjectDTO

    /**
     * Возвращает проект по его уникальному идентификатору.
     *
     * @param id Идентификатор проекта.
     * @return DTO проекта с указанным id.
     */
    suspend fun getProjectById(id: Long): ProjectDTO?

    /**
     * Возвращает список всех проектов.
     *
     * @return Список DTO всех проектов.
     */
    suspend fun getAllProjects(): List<ProjectDTO>

    /**
     * Обновляет данные проекта с указанным идентификатором.
     *
     * @param id Идентификатор проекта, который требуется обновить.
     * @param projectDTO DTO с новыми данными проекта.
     * @return DTO обновленного проекта или null, если проект не найден.
     */
    suspend fun updateProject(id: Long, projectDTO: ProjectDTO): ProjectDTO?

    /**
     * Удаляет проект с указанным идентификатором.
     *
     * @param id Идентификатор проекта для удаления.
     * @return true, если проект успешно удален, иначе false.
     */
    suspend fun deleteProject(id: Long): Boolean
}
